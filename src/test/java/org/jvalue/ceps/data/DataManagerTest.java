package org.jvalue.ceps.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jvalue.ceps.db.OdsRegistrationRepository;
import org.jvalue.ceps.ods.OdsClient;
import org.jvalue.ceps.ods.OdsDataSource;
import org.jvalue.ceps.ods.OdsDataSourceService;
import org.jvalue.ceps.ods.OdsNotificationService;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;

import static org.jvalue.ceps.ods.OdsNotificationService.OdsClientDescription;

@RunWith(JMockit.class)
public final class DataManagerTest {

	private final String sourceId = "someSourceId";

	@Mocked private OdsDataSourceService sourceService;
	@Mocked private OdsNotificationService notificationService;
	@Mocked private OdsRegistrationRepository repository;
	@Mocked private DataUpdateListener updateListener;

	private final String cepsBaseUrl = "http://localhost:8080";
	private final String dataUrl = "/data";

	private DataManager dataManager;

	@Mocked private OdsDataSource source;
	@Mocked private OdsClient client;

	private final ObjectNode sourceSchema = new ObjectNode(JsonNodeFactory.instance);
	{
		sourceSchema.put("key", "value");
	}


	@Before
	public void setupDataManager() {
		dataManager = new DataManager(
				sourceService,
				notificationService,
				repository,
				updateListener,
				cepsBaseUrl,
				dataUrl);
	}


	@Test
	public void testStartMonitoring() {
		setupSource();
		new Expectations() {{
			sourceService.get(anyString);
			result = source;

			notificationService.register(anyString, anyString, (OdsClientDescription) any);
			result = client;
		}};

		dataManager.startMonitoring(sourceId);

		new Verifications() {{
			sourceService.get(sourceId);
			times = 1;

			OdsClientDescription clientDescription;
			notificationService.register(sourceId, anyString, clientDescription = withCapture());

			Assert.assertEquals(cepsBaseUrl + dataUrl, clientDescription.getCallbackUrl());
			Assert.assertEquals(true, clientDescription.getSendData());

			updateListener.onSourceAdded(sourceId, sourceSchema);
			times = 1;
		}};
	}


	@Test
	public void testStopMonitoring() {
		setupSource();
		new Expectations() {{
			List<OdsRegistration> registrations = new LinkedList<>();
			registrations.add(new OdsRegistration(source, client));

			repository.getAll();
			result = registrations;
		}};

		dataManager.stopMonitoring(sourceId);

		new Verifications() {{
			notificationService.unregister(sourceId, anyString);

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
		setupSource();
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


	private void setupSource() {
		new Expectations() {{
			source.getSchema();
			result = sourceSchema;
			source.getId();
			minTimes = 0;
			result = sourceId;
		}};
	}



}
