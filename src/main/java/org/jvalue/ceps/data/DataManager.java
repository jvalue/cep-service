package org.jvalue.ceps.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;

import org.jvalue.ceps.db.OdsRegistrationRepository;
import org.jvalue.ceps.esper.DataUpdateListener;
import org.jvalue.ceps.ods.DataSourceService;
import org.jvalue.ceps.ods.NotificationService;
import org.jvalue.ceps.ods.OdsClient;
import org.jvalue.ceps.ods.OdsDataSource;
import org.jvalue.ceps.utils.Assert;
import org.jvalue.ceps.utils.Log;

import java.util.List;

import io.dropwizard.lifecycle.Managed;

import static org.jvalue.ceps.ods.NotificationService.OdsClientDescription;


public final class DataManager implements Managed, DataSink {

	private final DataSourceService dataSourceService;
	private final NotificationService notificationService;

	private final OdsRegistrationRepository registrationRepository;
	private final DataUpdateListener dataListener;


	@Inject
	DataManager(
			DataSourceService dataSourceService,
			NotificationService notificationService,
			OdsRegistrationRepository registrationRepository,
			DataUpdateListener dataListener) {

		this.dataSourceService = dataSourceService;
		this.notificationService = notificationService;
		this.registrationRepository = registrationRepository;
		this.dataListener = dataListener;
	}


	public void startMonitoring(
			String sourceId,
			String restCallbackUrl,
			String restCallbackParam) {

		Assert.assertNotNull(sourceId, restCallbackUrl, restCallbackParam);
		Assert.assertTrue(isBeingMonitored(sourceId), "source already being monitored");

		// get source / schema
		OdsDataSource source = dataSourceService.get(sourceId);

		// register for updates
		OdsClientDescription clientDescription = new OdsClientDescription(restCallbackUrl, restCallbackParam, true);
		OdsClient client = notificationService.register(sourceId, "ceps", clientDescription);

		// store result in db
		OdsRegistration registration = new OdsRegistration(source, client);
		registrationRepository.add(registration);
	}


	public void stopMonitoring(String sourceId) {
		Assert.assertNotNull(sourceId);
		OdsRegistration registration = getRegistrationForSourceId(sourceId);
		Assert.assertTrue(registration != null, "source not being monitored");

		notificationService.unregister(sourceId, registration.getClient().getId());
		registrationRepository.remove(registration);
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
		dataListener.onNewData(sourceId, data);
	}


	private OdsRegistration getRegistrationForSourceId(String sourceId) {
		for (OdsRegistration registration : registrationRepository.getAll()) {
			if (registration.getDataSource().getId().equals(sourceId)) return registration;
		}
		return null;
	}


	@Override
	public void start() {
		for (OdsRegistration registration : registrationRepository.getAll()) {
			dataListener.onNewDataType(
					registration.getDataSource().getId(),
					registration.getDataSource().getSchema());
		}
	}


	@Override
	public void stop() {
		// nothing to do
	}

}
