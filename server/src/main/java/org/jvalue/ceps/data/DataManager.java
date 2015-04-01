package org.jvalue.ceps.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import org.jvalue.ceps.api.data.OdsRegistration;
import org.jvalue.ceps.db.OdsRegistrationRepository;
import org.jvalue.ceps.main.ConfigModule;
import org.jvalue.ceps.rest.RestModule;
import org.jvalue.ceps.utils.Assert;
import org.jvalue.ceps.utils.Log;
import org.jvalue.ods.api.DataSourceApi;
import org.jvalue.ods.api.NotificationApi;
import org.jvalue.ods.api.notifications.ClientDescription;
import org.jvalue.ods.api.notifications.HttpClient;
import org.jvalue.ods.api.notifications.HttpClientDescription;
import org.jvalue.ods.api.sources.DataSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.dropwizard.lifecycle.Managed;
import retrofit.RetrofitError;


public final class DataManager implements Managed, DataSink {

	// Name of this HTTP client on the ODS. Might need more dynamic approach in the future.
	private static final String ODS_CLIENT_ID = "ceps";

	private final DataSourceApi odsDataSourceApi;
	private final NotificationApi odsNotificationApi;

	private final OdsRegistrationRepository registrationRepository;
	private final DataUpdateListener dataListener;

	private final String cepsDataCallbackUrl;


	@Inject
	DataManager(
			DataSourceApi odsDataSourceApi,
			NotificationApi odsNotificationApi,
			OdsRegistrationRepository registrationRepository,
			DataUpdateListener dataListener,
			@Named(ConfigModule.BASE_URL) String cepsBaseUrl,
			@Named(RestModule.URL_DATA) String dataUrl) {

		this.odsDataSourceApi = odsDataSourceApi;
		this.odsNotificationApi = odsNotificationApi;
		this.registrationRepository = registrationRepository;
		this.dataListener = dataListener;
		this.cepsDataCallbackUrl = cepsBaseUrl + dataUrl;
	}


	public OdsRegistration startMonitoring(String sourceId) {
		Assert.assertNotNull(sourceId);
		if (isBeingMonitored(sourceId)) throw new IllegalStateException("source already being monitored");

		// get source / schema
		DataSource source = odsDataSourceApi.getSourceSynchronously(sourceId);

		// register for updates
		ClientDescription clientDescription = new HttpClientDescription(cepsDataCallbackUrl, true);
		HttpClient client = (HttpClient) odsNotificationApi.registerClientSynchronously(sourceId, ODS_CLIENT_ID, clientDescription);

		// store result in db
		OdsRegistration registration = new OdsRegistration(sourceId, source, client);
		registrationRepository.add(registration);

		// notify listener
		dataListener.onSourceAdded(sourceId, source.getSchema());

		return registration;
	}


	public void stopMonitoring(String sourceId) {
		Assert.assertNotNull(sourceId);
		OdsRegistration registration = getRegistrationForSourceId(sourceId);
		if (registration == null) throw new IllegalStateException("source not being monitored");

		try {
			odsNotificationApi.unregisterClientSynchronously(sourceId, registration.getClient().getId());
		} catch (RetrofitError re) {
			Log.error("failed to unregister from ODS", re);
		} finally {
			registrationRepository.remove(registration);
			dataListener.onSourceRemoved(sourceId, registration.getDataSource().getSchema());
		}
	}


	public boolean isBeingMonitored(String sourceId) {
		Assert.assertNotNull(sourceId);
		return getRegistrationForSourceId(sourceId) != null;
	}


	public List<OdsRegistration> getAll() {
		return registrationRepository.getAll();
	}


	public OdsRegistration get(String sourceId) {
		return registrationRepository.findById(sourceId);
	}


	@Override
	public void onNewData(String sourceId, ArrayNode data) {
		Log.info("Source " + sourceId + " has new data (" + data.size() + " items)");
		dataListener.onNewSourceData(sourceId, data);
	}


	private OdsRegistration getRegistrationForSourceId(String sourceId) {
		for (OdsRegistration registration : registrationRepository.getAll()) {
			if (registration.getDataSource().getId().equals(sourceId)) return registration;
		}
		return null;
	}


	@Override
	public void start() {
		Map<String, JsonNode> sources = new HashMap<>();
		for (OdsRegistration registration : registrationRepository.getAll()) {
			// check if source is still present on ODS, otherwise remove



			sources.put(registration.getDataSource().getId(), registration.getDataSource().getSchema());
		}
		dataListener.onRestoreSources(sources);
	}


	@Override
	public void stop() {
		// nothing to do
	}

}
