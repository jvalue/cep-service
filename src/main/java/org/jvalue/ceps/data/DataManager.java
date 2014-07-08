package org.jvalue.ceps.data;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.jvalue.ceps.utils.Assert;
import org.jvalue.ceps.utils.RestCall;
import org.jvalue.ceps.utils.RestException;


public final class DataManager {

	private static final String
		ODS_URL_REGISTRATION = "/notifications/rest/register",
		ODS_URL_UNREGISTRATION = "/notifications/rest/unregister",
		ODS_PARAM_CLIENT_ID = "regId",
		ODS_PARAM_SOURCE = "source",
		ODS_PARAM_CEPS_URL = "restUrl",
		ODS_PARAM_CEPS_SOURCE = "restParam";


	private static DataManager instance;

	public static DataManager getInstance() {
		if (instance == null) instance = new DataManager();
		return instance;
	}


	private final Set<DataSource> sources = new HashSet<DataSource>();
	private final String clientId = UUID.randomUUID().toString();

	private DataManager() { }


	public void startMonitoring(
			DataSource source, 
			String restCallbackUrl, 
			String restCallbackParam) throws RestException {

		Assert.assertNotNull(source, restCallbackUrl, restCallbackParam);
		Assert.assertFalse(sources.contains(source), "source already being monitored");

		updateMonitoring(source, ODS_URL_REGISTRATION, restCallbackUrl, restCallbackParam);
		sources.add(source);
	}


	public void stopMonitoring(DataSource source) throws RestException {
		Assert.assertNotNull(source);
		Assert.assertTrue(sources.contains(source), "source not being monitored");

		updateMonitoring(source, ODS_URL_UNREGISTRATION, null, null);
		sources.remove(source);
	}


	private void updateMonitoring(
			DataSource source, 
			String odsRegistrationUrl,
			String restCallbackUrl,
			String restCallbackParam) throws RestException {

		new RestCall.Builder(RestCall.RequestType.POST, source.getOdsUrl())
			.path(odsRegistrationUrl)
			.parameter(ODS_PARAM_CLIENT_ID, clientId)
			.parameter(ODS_PARAM_SOURCE, source.getOdsId())
			.parameter(ODS_PARAM_CEPS_URL, restCallbackUrl)
			.parameter(ODS_PARAM_CEPS_SOURCE, restCallbackParam)
			.build()
			.execute();
	}


	public void onSourceChanged(String sourceId) {
		System.err.println("Source " + sourceId + " changed!");
	}


	public void registerDataListener(DataListener listener) {
		throw new UnsupportedOperationException("working on it ...");
	}


	public void unregisterDataListener(DataListener listener) {
		throw new UnsupportedOperationException("working on it ...");
	}

}
