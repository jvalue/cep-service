package org.jvalue.ceps.esper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URL;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;


public final class EsperManagerTest {

	private static final ObjectMapper mapper = new ObjectMapper();
	private static final String dataType = "pegelonline";

	private int updateCount = 0;


	@Test
	public void testEventUpdates() throws Exception {

		EsperManager manager = EsperManager.getInstance();
		assertNotNull(manager);

		String eplStmt = 
			"select * from "
			+ dataType 
			+ ".win:length(1) where longname = 'EITZE' and BodyOfWater.longname = 'ALLER'";

		JsonNode schema = getResource("/schema-pegelonline.json");
		JsonNode data = new ArrayNode(JsonNodeFactory.instance)
			.add(getResource("/data-pegelonline-eitze.json"));

		manager.onNewDataType(dataType, schema);
		String regId = manager.register(eplStmt, new DummyUpdateListener());
		manager.onNewData(dataType, data);

		assertNotNull(regId);
		assertEquals(1, updateCount);

		manager.unregister(regId);
		manager.onNewData(dataType, data);

		assertEquals(1, updateCount);

	}


	private JsonNode getResource(String resourceName) throws Exception {
		URL url = getClass().getResource(resourceName);
		return mapper.readTree(new File(url.toURI()));
	}


	private class DummyUpdateListener implements JsonUpdateListener {
		@Override
		public void onNewEvents(List<JsonNode> newEvents, List<JsonNode> oldEvents) {
			assertNotNull(newEvents);
			assertNotNull(oldEvents);
			assertTrue(newEvents.size() > 0);
			updateCount++;
		}
	}

}
