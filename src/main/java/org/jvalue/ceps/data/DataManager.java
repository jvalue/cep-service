package org.jvalue.ceps.data;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.jvalue.ceps.db.DbAccessorFactory;
import org.jvalue.ceps.db.JsonObjectDb;
import org.jvalue.ceps.utils.Assert;
import org.jvalue.ceps.utils.Log;
import org.jvalue.ceps.utils.RestCall;
import org.jvalue.ceps.utils.RestException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


public final class DataManager {

	private static final String DB_NAME = "cepsOdsSources";

	private static final String
		ODS_URL_REGISTRATION = "notifications/rest/register",
		ODS_URL_UNREGISTRATION = "notifications/unregister",
		ODS_PARAM_SOURCE = "source",
		ODS_PARAM_SEND_DATA = "sendData",
		ODS_PARAM_CEPS_URL = "restUrl",
		ODS_PARAM_CEPS_SOURCE = "restParam",
		ODS_PARAM_CLIENT_ID = "clientId";

	private static final String
		ODS_KEY_CLIENTID = "clientId";


	private static final ObjectMapper mapper = new ObjectMapper();
	private static DataManager instance;

	public static DataManager getInstance() {
		if (instance == null) instance = new DataManager(
				new JsonObjectDb<DataSourceRegistration>(
					DbAccessorFactory.getCouchDbAccessor(DB_NAME), 
					DataSourceRegistration.class));
		return instance;
	}


	private final List<DataChangeListener> listeners = new LinkedList<DataChangeListener>();
	private final JsonObjectDb<DataSourceRegistration> sourceDb;

	private DataManager(JsonObjectDb<DataSourceRegistration> sourceDb) {
		Assert.assertNotNull(sourceDb);
		this.sourceDb = sourceDb;
	}


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
				listener.onNewDataType(source.getOdsSourceId(), dataSchema);
			}
		} catch (IOException ioe) {
			throw new RestException(ioe);
		}

		// register for updates
		String jsonResult = new RestCall.Builder(RestCall.RequestType.POST, source.getOdsUrl())
			.path(ODS_URL_REGISTRATION)
			.parameter(ODS_PARAM_SOURCE, source.getOdsSourceId())
			.parameter(ODS_PARAM_SEND_DATA, Boolean.TRUE.toString())
			.parameter(ODS_PARAM_CEPS_URL, restCallbackUrl)
			.parameter(ODS_PARAM_CEPS_SOURCE, restCallbackParam)
			.build()
			.execute();

		String clientId;
		try {
			JsonNode json = mapper.readTree(jsonResult);
			clientId = json.get(ODS_KEY_CLIENTID).asText();
		} catch (IOException ioe) {
			throw new RestException(ioe);
		}

		DataSourceRegistration registration = new DataSourceRegistration(clientId, source);
		sourceDb.add(registration);
	}


	public void stopMonitoring(DataSource source) throws RestException {
		Assert.assertNotNull(source);
		DataSourceRegistration registration = getRegistrationForSource(source);
		Assert.assertTrue(registration != null, "source not being monitored");

		new RestCall.Builder(RestCall.RequestType.POST, source.getOdsUrl())
			.path(ODS_URL_UNREGISTRATION)
			.parameter(ODS_PARAM_CLIENT_ID, registration.getClientId())
			.build()
			.execute();

		sourceDb.remove(registration);
	}


	public boolean isBeingMonitored(DataSource source) {
		Assert.assertNotNull(source);
		return getRegistrationForSource(source) != null;
	}


	public void onSourceChanged(String sourceId, JsonNode data) {
		Log.info("Source " + sourceId + " has new data");
		for (DataChangeListener listener : listeners) {
			listener.onNewData(sourceId, data);
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


	private DataSourceRegistration getRegistrationForSource(DataSource source) {
		for (DataSourceRegistration registration : sourceDb.getAll()) {
			if (registration.getDataSource().equals(source)) {
				return registration;
			}
		}
		return null;
	}

}
