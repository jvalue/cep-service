package org.jvalue.ceps.data;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jvalue.ceps.db.OdsRegistrationRepository;
import org.jvalue.ods.api.notifications.ClientDescription;
import org.jvalue.ods.api.notifications.HttpClient;
import org.jvalue.ods.api.notifications.HttpClientDescription;
import org.jvalue.ods.api.notifications.NotificationApi;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.api.sources.DataSourceApi;
import org.jvalue.ods.api.sources.DataSourceMetaData;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;

@RunWith(JMockit.class)
public final class DataManagerTest {

	private final String sourceId = "someSourceId";
	private final ObjectNode sourceSchema = new ObjectNode(JsonNodeFactory.instance);
	{
		sourceSchema.put("key", "value");
	}

	@Mocked private DataSourceApi sourceApi;
	@Mocked private NotificationApi notificationApi;
	@Mocked private OdsRegistrationRepository repository;
	@Mocked private DataUpdateListener updateListener;

	private final String cepsBaseUrl = "http://localhost:8080";
	private final String dataUrl = "/data";

	private DataManager dataManager;

	private final DataSource source = new DataSource(
			sourceId,
			JsonPointer.compile("/id"),
			sourceSchema,
			new DataSourceMetaData("", "", "", "", "", "", ""));
	private final HttpClient client = new HttpClient("id", "http://localhost", false);



	@Before
	public void setupDataManager() {
		dataManager = new DataManager(
				sourceApi,
				notificationApi,
				repository,
				updateListener,
				cepsBaseUrl,
				dataUrl);
	}


	@Test
	public void testStartMonitoring() {
		new Expectations() {{
			sourceApi.get(anyString);
			result = source;

			notificationApi.register(anyString, anyString, (ClientDescription) any);
			result = client;
		}};

		dataManager.startMonitoring(sourceId);

		new Verifications() {{
			sourceApi.get(sourceId);
			times = 1;

			HttpClientDescription clientDescription;
			notificationApi.register(sourceId, anyString, clientDescription = withCapture());

			Assert.assertEquals(cepsBaseUrl + dataUrl, clientDescription.getCallbackUrl());
			Assert.assertEquals(true, clientDescription.getSendData());

			updateListener.onSourceAdded(sourceId, sourceSchema);
			times = 1;
		}};
	}


	@Test
	public void testStopMonitoring() {
		new Expectations() {{
			List<OdsRegistration> registrations = new LinkedList<>();
			registrations.add(new OdsRegistration(source, client));

			repository.getAll();
			result = registrations;
		}};

		dataManager.stopMonitoring(sourceId);

		new Verifications() {{
			notificationApi.unregister(sourceId, anyString);

			updateListener.onSourceRemoved(sourceId, sourceSchema); times = 1;
		}};
	}


	@Test
	public void testOnNewData() {
		final ArrayNode data = new ArrayNode(JsonNodeFactory.instance);
		data.add("value1");
		data.add("value2");

		dataManager.onNewData(sourceId, data);

		new Verifications() {{
			updateListener.onNewSourceData(sourceId, data);
		}};
	}


	@Test
	public  void testStart() {
		new Expectations() {{
			List<OdsRegistration> registrations = new LinkedList<>();
			registrations.add(new OdsRegistration(source, client));

			repository.getAll();
			result = registrations;
		}};

		dataManager.start();

		new Verifications() {{
			Map<String, JsonNode> sources;
			updateListener.onRestoreSources(sources = withCapture());

			Assert.assertTrue(sources.containsKey(sourceId));
			Assert.assertEquals(sourceSchema, sources.get(sourceId));
		}};
	}

}
