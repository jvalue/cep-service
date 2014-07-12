package org.jvalue.ceps.data;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.jvalue.ceps.utils.Assert;
import org.jvalue.ceps.utils.Log;
import org.jvalue.ceps.utils.RestCall;
import org.jvalue.ceps.utils.RestException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


public final class DataManager {

	private static final String
		ODS_URL_REGISTRATION = "notifications/rest/register",
		ODS_URL_UNREGISTRATION = "notifications/rest/unregister",
		ODS_PARAM_CLIENT_ID = "regId",
		ODS_PARAM_SOURCE = "source",
		ODS_PARAM_SEND_DATA = "sendData",
		ODS_PARAM_CEPS_URL = "restUrl",
		ODS_PARAM_CEPS_SOURCE = "restParam";


	private static final ObjectMapper mapper = new ObjectMapper();
	private static DataManager instance;

	public static DataManager getInstance() {
		if (instance == null) instance = new DataManager();
		return instance;
	}


	private final String clientId = UUID.randomUUID().toString();
	private final DataSourceDb sourceDb = DataSourceDb.getInstance();
	private final List<DataChangeListener> listeners = new LinkedList<DataChangeListener>();

	private DataManager() { }


	public void startMonitoring(
			DataSource source, 
			String restCallbackUrl, 
			String restCallbackParam) throws RestException {

		Assert.assertNotNull(source, restCallbackUrl, restCallbackParam);
		Assert.assertFalse(sourceDb.getAll().contains(source), "source already being monitored");


		// get new schema from ods
		String dataSchemaString = new RestCall.Builder(
				RestCall.RequestType.GET, 
				source.getOdsUrl())
			.path(source.getOdsSchemaUrl())
			.build()
			.execute();

		try {
			JsonNode dataSchema = mapper.readTree(dataSchemaString);
			for (DataChangeListener listener : listeners) {
				listener.onNewDataType(source.getOdsId(), dataSchema);
			}
		} catch (IOException ioe) {
			throw new RestException(ioe);
		}

		// register for updates
		updateMonitoring(source, ODS_URL_REGISTRATION, restCallbackUrl, restCallbackParam);
		sourceDb.add(source);
	}


	public void stopMonitoring(
			DataSource source,
			String restCallbackUrl,
			String restCallbackParam) throws RestException {

		Assert.assertNotNull(source);
		Assert.assertTrue(sourceDb.getAll().contains(source), "source not being monitored");

		updateMonitoring(source, ODS_URL_UNREGISTRATION, restCallbackUrl, restCallbackParam);
		sourceDb.remove(source);
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
			.parameter(ODS_PARAM_SEND_DATA, Boolean.TRUE.toString())
			.parameter(ODS_PARAM_CEPS_URL, restCallbackUrl)
			.parameter(ODS_PARAM_CEPS_SOURCE, restCallbackParam)
			.build()
			.execute();
	}


	public boolean isBeingMonitored(DataSource source) {
		Assert.assertNotNull(source);
		return sourceDb.getAll().contains(source);
	}


	public void onSourceChanged(String sourceId, JsonNode data) {
		Log.info("Source " + sourceId + " has new data");
		for (DataChangeListener listener : listeners) {
			listener.onNewData(data);
		}
	}


	public void registerDataListener(DataChangeListener listener) {
		Assert.assertNotNull(listener);
		listeners.add(listener);
	}


	public void unregisterDataListener(DataChangeListener listener) {
		Assert.assertNotNull(listener);
		listeners.remove(listener);
	}

}
