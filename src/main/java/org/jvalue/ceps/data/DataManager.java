package org.jvalue.ceps.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.jvalue.ceps.db.DbAccessorFactory;
import org.jvalue.ceps.db.JsonObjectDb;
import org.jvalue.ceps.esper.DataUpdateListener;
import org.jvalue.ceps.esper.EsperManager;
import org.jvalue.ceps.utils.Assert;
import org.jvalue.ceps.utils.Log;
import org.jvalue.ceps.utils.RestCall;
import org.jvalue.ceps.utils.RestException;
import org.jvalue.ceps.utils.Restoreable;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;


public final class DataManager implements Restoreable {

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
					DataSourceRegistration.class),
				EsperManager.getInstance());
		return instance;
	}


	private final JsonObjectDb<DataSourceRegistration> sourceDb;
	private final DataUpdateListener dataListener;

	private DataManager(
			JsonObjectDb<DataSourceRegistration> sourceDb,
			DataUpdateListener dataListener) {

		Assert.assertNotNull(sourceDb, dataListener);
		this.sourceDb = sourceDb;
		this.dataListener = dataListener;
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
				source.getServerBaseUrl().toString())
			.path(source.getDataSchemaUrl())
			.build()
			.execute();

		JsonNode dataSchema = null;
		try {
			dataSchema = mapper.readTree(dataSchemaString);
			dataListener.onNewDataType(source.getSourceId(), dataSchema);
		} catch (IOException ioe) {
			throw new RestException(ioe);
		}

		// register for updates
		String jsonResult = new RestCall.Builder(RestCall.RequestType.POST, source.getServerBaseUrl().toString())
			.path(ODS_URL_REGISTRATION)
			.parameter(ODS_PARAM_SOURCE, source.getSourceId())
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

		DataSourceRegistration registration = new DataSourceRegistration(
				clientId, 
				source, 
				dataSchema);
		sourceDb.add(registration);
	}


	public void stopMonitoring(DataSource source) throws RestException {
		Assert.assertNotNull(source);
		DataSourceRegistration registration = getRegistrationForSource(source);
		Assert.assertTrue(registration != null, "source not being monitored");

		new RestCall.Builder(RestCall.RequestType.POST, source.getServerBaseUrl().toString())
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


	public Set<DataSourceRegistration> getAll() {
		return new HashSet<DataSourceRegistration>(sourceDb.getAll());
	}


	public void onSourceChanged(String sourceId, JsonNode data) {
		Log.info("Source " + sourceId + " has new data");
		dataListener.onNewData(sourceId, data);
	}


	private DataSourceRegistration getRegistrationForSource(DataSource source) {
		for (DataSourceRegistration registration : sourceDb.getAll()) {
			if (registration.getDataSource().equals(source)) {
				return registration;
			}
		}
		return null;
	}


	@Override
	public void restoreState() {
		Log.info("Restoring state for " + DataManager.class.getSimpleName());
		for (DataSourceRegistration registration : sourceDb.getAll()) {
			dataListener.onNewDataType(
					registration.getDataSource().getSourceId(),
					registration.getDataSchema());
		}
	}

}
