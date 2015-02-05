package org.jvalue.ceps.esper;

import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;


@RunWith(JMockit.class)
public final class EsperManagerTest {

	private static final String SCHEMA_NAME = "someSchema";

	@Mocked SchemaTranslator schemaTranslator;
	@Mocked DataTranslator dataTranslator;

	@Mocked EventUpdateListener updateListener;


	private EsperManager esperManager;
	private EPServiceProvider epServiceProvider;

	@Before
	public void setupManager() {
		epServiceProvider = EPServiceProviderManager.getProvider(EsperManagerTest.class.getSimpleName());
		esperManager = new EsperManager(epServiceProvider, schemaTranslator, dataTranslator);
	}


	@After
	public void tearDownManager() {
		epServiceProvider.destroy();
	}


	@Test
	@SuppressWarnings("unchecked")
	public void testOnNewData() throws Exception {
		new Expectations() {{
			schemaTranslator.toEventDefinition(SCHEMA_NAME, (JsonNode) any);
			result = createDummySchema(SCHEMA_NAME);

			dataTranslator.toMap((JsonNode) any);
			result = createDummyData();
		}};

		String eplStmt =
				"select * from "
				+ SCHEMA_NAME
				+ ".win:length(1) where attribute1 = 'some value'";

		final int dataItems = 3;
		ArrayNode dummyJsonData = new ArrayNode(JsonNodeFactory.instance);
		for (int i = 0; i < dataItems; ++i) dummyJsonData.add(0);

		esperManager.onSourceAdded(SCHEMA_NAME, new ObjectNode(JsonNodeFactory.instance));
		final String regId = esperManager.register(eplStmt, updateListener);
		esperManager.onNewSourceData(SCHEMA_NAME, dummyJsonData);

		Assert.assertNotNull(regId);
		new Verifications() {{
			updateListener.onNewEvents(regId, (List<JsonNode>) any, (List<JsonNode>) any);
			times = dataItems;
		}};
	}


	private EventDefinition createDummySchema(String schemaName) {
		Map<String, Object> schema = new HashMap<>();
		schema.put("attribute1", String.class);
		schema.put("attribute2", double.class);
		return new EventDefinition(schemaName, schema);
	}


	private Map<String, Object> createDummyData() {
		Map<String, Object> data = new HashMap<>();
		data.put("attribute1", "some value");
		data.put("attribute2", 1234);
		return data;
	}

}
