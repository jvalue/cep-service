package org.jvalue.ceps.esper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URL;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


public final class EsperManagerTest {

	private static final ObjectMapper mapper = new ObjectMapper();
	private static final String dataType = "pegelonline";

	private int updateCount = 0;


	@Before
	public final void resetCounter() {
		updateCount = 0;
	}


	@Test
	public void testEventUpdates() throws Exception {

		EsperManager manager = DummyEsperManager.createInstance("EsperManagerTest");
		assertNotNull(manager);

		String eplStmt = 
			"select timeseries[0] from "
			+ dataType 
			+ ".win:length(1) where "
			+ "longname = 'EITZE' "
			+ "and BodyOfWater.longname = 'ALLER' "
			+ "and timeseries[0].shortname = 'W'";

		JsonNode schema = getResource("/schema-pegelonline.json");
		JsonNode data = getResource("/data-pegelonline1.json");

		manager.onNewDataType(dataType, schema);
		String regId = manager.register(eplStmt, new DummyUpdateListener());
		manager.onNewData(dataType, data);

		assertNotNull(regId);
		assertEquals(1, updateCount);

		manager.unregister(regId);
		manager.onNewData(dataType, data);

		assertEquals(1, updateCount);

	}


	@Test
	public void testComplexEventUpdate() throws Exception {

		EsperManager manager = DummyEsperManager.createInstance("EsperManagerComplexTest");
		assertNotNull(manager);

		String station = "EITZE";
		String river = "ALLER";
		String filter = "(longname = '" + station + "' " 
			+ "and BodyOfWater.longname = '" + river + "' "
			+ "and timeseries.firstof(i => i.shortname = 'W') is not null)";
		double level = 300;

		String eplStmt = 
			"select a, b from pattern [every "
			+ "a=" + dataType + filter + " -> b=" + dataType + filter + "] "
			+ "where "
			+ "a.timeseries.firstof(i => i.shortname = 'W' and i.currentMeasurement.value <= " + level + ") "
			+ "is not null and "
			+ "b.timeseries.firstof(i => i.shortname = 'W' and i.currentMeasurement.value > " + level + ") "
			+ " is not null";

		JsonNode schema = getResource("/schema-pegelonline.json");
		int dataCount = 4;
		JsonNode[] data = new JsonNode[dataCount];
		for (int i = 0; i < dataCount; i++) {
			data[i] = getResource("/data-pegelonline" + (i+1) + ".json");
		}

		manager.onNewDataType(dataType, schema);
		manager.register(eplStmt, new DummyUpdateListener());

		manager.onNewData(dataType, data[0]);
		manager.onNewData(dataType, data[1]);
		assertEquals(0, updateCount);
		manager.onNewData(dataType, data[2]);
		manager.onNewData(dataType, data[3]);
		assertEquals(1, updateCount);
	}


	private JsonNode getResource(String resourceName) throws Exception {
		URL url = getClass().getResource(resourceName);
		return mapper.readTree(new File(url.toURI()));
	}


	private class DummyUpdateListener implements JsonUpdateListener {
		@Override
		public void onNewEvents(String stmtId, List<JsonNode> newEvents, List<JsonNode> oldEvents) {
			assertNotNull(stmtId);
			assertNotNull(newEvents);
			assertNotNull(oldEvents);
			assertTrue(newEvents.size() > 0);
			updateCount++;
		}
	}

}
