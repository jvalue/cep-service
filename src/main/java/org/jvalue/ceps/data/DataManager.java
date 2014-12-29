package org.jvalue.ceps.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import org.jvalue.ceps.db.OdsRegistrationRepository;
import org.jvalue.ceps.main.ConfigModule;
import org.jvalue.ceps.ods.OdsClient;
import org.jvalue.ceps.ods.OdsDataSource;
import org.jvalue.ceps.ods.OdsDataSourceService;
import org.jvalue.ceps.ods.OdsNotificationService;
import org.jvalue.ceps.rest.RestModule;
import org.jvalue.ceps.utils.Assert;
import org.jvalue.ceps.utils.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.dropwizard.lifecycle.Managed;

import static org.jvalue.ceps.ods.OdsNotificationService.OdsClientDescription;


public final class DataManager implements Managed, DataSink {

	// Name of this HTTP client on the ODS. Might need more dynamic approach in the future.
	private static final String ODS_CLIENT_ID = "ceps";

	private final OdsDataSourceService odsDataSourceService;
	private final OdsNotificationService odsNotificationService;

	private final OdsRegistrationRepository registrationRepository;
	private final DataUpdateListener dataListener;

	private final String cepsDataCallbackUrl;


	@Inject
	DataManager(
			OdsDataSourceService odsDataSourceService,
			OdsNotificationService odsNotificationService,
			OdsRegistrationRepository registrationRepository,
			DataUpdateListener dataListener,
			@Named(ConfigModule.CEPS_BASE_URL) String cepsBaseUrl,
			@Named(RestModule.URL_DATA) String dataUrl) {

		this.odsDataSourceService = odsDataSourceService;
		this.odsNotificationService = odsNotificationService;
		this.registrationRepository = registrationRepository;
		this.dataListener = dataListener;
		this.cepsDataCallbackUrl = cepsBaseUrl + dataUrl;
	}


	public void startMonitoring(String sourceId) {
		Assert.assertNotNull(sourceId);
		Assert.assertFalse(isBeingMonitored(sourceId), "source already being monitored");

		// get source / schema
		OdsDataSource source = odsDataSourceService.get(sourceId);

		// register for updates
		OdsClientDescription clientDescription = new OdsClientDescription(cepsDataCallbackUrl, true);
		OdsClient client = odsNotificationService.register(sourceId, ODS_CLIENT_ID, clientDescription);

		// store result in db
		OdsRegistration registration = new OdsRegistration(source, client);
		registrationRepository.add(registration);

		// notify listener
		dataListener.onSourceAdded(sourceId, source.getSchema());
	}


	public void stopMonitoring(String sourceId) {
		Assert.assertNotNull(sourceId);
		OdsRegistration registration = getRegistrationForSourceId(sourceId);
		Assert.assertTrue(registration != null, "source not being monitored");

		odsNotificationService.unregister(sourceId, registration.getClient().getId());
		registrationRepository.remove(registration);
		dataListener.onSourceRemoved(sourceId, registration.getDataSource().getSchema());
	}


	public boolean isBeingMonitored(String sourceId) {
		Assert.assertNotNull(sourceId);
		return getRegistrationForSourceId(sourceId) != null;
	}


	public List<OdsRegistration> getAll() {
		return registrationRepository.getAll();
	}


	@Override
	public void onNewData(String sourceId, JsonNode data) {
		Log.info("Source " + sourceId + " has new data");
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
			sources.put(registration.getDataSource().getId(), registration.getDataSource().getSchema());
		}
		dataListener.onRestoreSources(sources);
	}


	@Override
	public void stop() {
		// nothing to do
	}

}
