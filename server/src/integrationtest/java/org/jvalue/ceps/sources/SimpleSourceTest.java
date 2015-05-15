package org.jvalue.ceps.sources;


import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.jvalue.ceps.api.EplAdapterApi;
import org.jvalue.ceps.api.RegistrationApi;
import org.jvalue.ceps.api.SourcesApi;
import org.jvalue.ceps.api.adapter.ArgumentType;
import org.jvalue.ceps.api.adapter.EplAdapterDescription;
import org.jvalue.ceps.api.notifications.Client;
import org.jvalue.ceps.api.notifications.HttpClientDescription;
import org.jvalue.ods.api.DataSourceApi;
import org.jvalue.ods.api.NotificationApi;
import org.jvalue.ods.api.ProcessorChainApi;
import org.jvalue.ods.api.processors.ProcessorReference;
import org.jvalue.ods.api.processors.ProcessorReferenceChainDescription;
import org.jvalue.ods.api.sources.DataSourceDescription;
import org.jvalue.ods.api.sources.DataSourceMetaData;

import java.util.HashMap;
import java.util.Map;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.converter.JacksonConverter;

/**
 * A simple test that is supposed make one "full round" of communications
 * between the ODS and CEPS.
 *
 * Assumes that both the ODS and CEPS are running.
 */
public final class SimpleSourceTest {

	private static final ObjectMapper mapper = new ObjectMapper();

	private static final String
			SOURCE_ID = "ceps" + SimpleSourceTest.class.getSimpleName(),
			ADAPTER_ID = "ceps" + SimpleSourceTest.class.getSimpleName();

	private MockWebServer webServer;
	private String sourceUrl;
	private String clientUrl;

	private SourcesApi cepsSourceApi;
	private EplAdapterApi cepsAdapterApi;
	private RegistrationApi cepsRegistrationApi;
	private DataSourceApi odsSourceApi;
	private ProcessorChainApi odsProcessorApi;

	private String registeredClientId = null;


	@Before
	public void setupSourceAndOds() throws Exception {
		// setup data and start the mock web server
		webServer = new MockWebServer();
		final ArrayNode data = new ArrayNode(JsonNodeFactory.instance);
		for (int i = 0; i < 44; ++i) {
			ObjectNode object = data.addObject();
			object.put("id", String.valueOf(i));
			object.put("data", i);
		}

		// one request to fetch data and one callback from the CEPS to inform the client
		webServer.enqueue(new MockResponse().setBody(mapper.valueToTree(data).toString()));
		webServer.enqueue(new MockResponse().setResponseCode(200));
		webServer.play();

		sourceUrl = webServer.getUrl("/dataUrl").toString();
		clientUrl = webServer.getUrl("/clientUrl").toString();

		// setup ODS
		RestAdapter odsRestAdapter = new RestAdapter.Builder()
				.setConverter(new JacksonConverter())
				.setEndpoint("http://localhost:8080/ods/api/v1")
				.setRequestInterceptor(new RequestInterceptor() {
					@Override
					public void intercept(RequestFacade request) {
						// admin@adminland.com:admin123
						request.addHeader("Authorization", "Basic YWRtaW5AYWRtaW5sYW5kLmNvbTphZG1pbjEyMw==");
					}
				})
				.build();

		odsSourceApi = odsRestAdapter.create(DataSourceApi.class);
		odsProcessorApi = odsRestAdapter.create(ProcessorChainApi.class);

		JsonNode schema = mapper.readTree(
				"{ \"$schema\": \"http://json-schema.org/draft-04/schema#\","
						+ "\"type\": \"object\","
						+ "\"properties\": {"
						+ "\"id\": { \"type\": \"string\" },"
						+ "\"data\": { \"type\": \"number\" }"
						+ "}}");

		odsSourceApi.addSourceSynchronously(SOURCE_ID, new DataSourceDescription(JsonPointer.compile("/id"), schema, new DataSourceMetaData("", "", "", "", "", "", "")));

		// setup CEPS
		RestAdapter cepsRestAdapter = new RestAdapter.Builder()
				.setConverter(new JacksonConverter())
				.setEndpoint("http://localhost:8082/ceps/api/v1")
				.setRequestInterceptor(new RequestInterceptor() {
					@Override
					public void intercept(RequestFacade request) {
						// admin@adminland.com:admin123
						request.addHeader("Authorization", "Basic YWRtaW5AYWRtaW5sYW5kLmNvbTphZG1pbjEyMw==");
					}
				})
				.build();

		// add source to CEPS
		cepsSourceApi = cepsRestAdapter.create(SourcesApi.class);
		cepsSourceApi.addSourceSynchronously(SOURCE_ID);
		NotificationApi odsNotificationApi = odsRestAdapter.create(NotificationApi.class);
		Assert.assertNotNull(odsNotificationApi.getClient(SOURCE_ID, "ceps"));

		// add simple EPL adapter to CEPS
		cepsAdapterApi = cepsRestAdapter.create(EplAdapterApi.class);
		String adapterArgKey = "VALUE";
		Map<String, ArgumentType> requiredAdapterArgs = new HashMap<>();
		requiredAdapterArgs.put(adapterArgKey, ArgumentType.NUMBER);
		cepsAdapterApi.addAdapterSynchronously(
				ADAPTER_ID,
				new EplAdapterDescription("select object.data from " + SOURCE_ID + ".win:length(1) as object where object.data > " + adapterArgKey, requiredAdapterArgs));
		Assert.assertEquals(ADAPTER_ID, cepsAdapterApi.getAdapterSynchronously(ADAPTER_ID).getId());

		// add http client to CEPS
		Map<String, Object> adapterArgs = new HashMap<>();
		adapterArgs.put(adapterArgKey, 42);
		cepsRegistrationApi = cepsRestAdapter.create(RegistrationApi.class);
		Client registeredClient = cepsRegistrationApi.registerClientSynchronously(SOURCE_ID, new HttpClientDescription(clientUrl, adapterArgs));
		Assert.assertEquals(registeredClient, cepsRegistrationApi.getClientSynchronously(SOURCE_ID, registeredClient.getId()));
		this.registeredClientId = registeredClient.getId();
	}


	@After
	public void cleanup() throws Exception {
		// stop source server
		webServer.shutdown();

		// remove client, adapter and source from CEPS
		if (cepsRegistrationApi != null) cepsRegistrationApi.unregisterClientSynchronously(SOURCE_ID, registeredClientId);
		if (cepsAdapterApi != null) cepsAdapterApi.deleteAdapterSynchronously(ADAPTER_ID);
		if (cepsSourceApi != null) cepsSourceApi.deleteSourceSynchronously(SOURCE_ID);

		// remove source from ODS
		if (odsSourceApi != null) odsSourceApi.deleteSourceSynchronously(SOURCE_ID);
	}


	@Test
	public void testSimpleSource() throws Exception {
		// give ODS and CEPS some time to settle
		Thread.sleep(500);

		// setup one time pull for ODS
		Map <String, Object> adapterParams = new HashMap<>();
		adapterParams.put("sourceUrl", sourceUrl);
		Map <String, Object> dbParams = new HashMap<>();
		dbParams.put("updateData", false);

		odsProcessorApi.addProcessorChainSynchronously(
				SOURCE_ID,
				"myProcessor",
				new ProcessorReferenceChainDescription
						.Builder(null)
						.processor(new ProcessorReference("JsonSourceAdapter", adapterParams))
						.processor(new ProcessorReference("NotificationFilter", new HashMap<String, Object>()))
						.processor(new ProcessorReference("DbInsertionFilter", dbParams))
						.build());

		// one request for fetching data from mock server
		webServer.takeRequest();

		// one callback request to mock server which also acts as the client
		RecordedRequest clientCallbackRequest = webServer.takeRequest();

		JsonNode jsonResult = mapper.readTree(clientCallbackRequest.getBody());
		Assert.assertTrue(jsonResult.has("clientId"));
		Assert.assertTrue(jsonResult.has("eventId"));
	}

}
