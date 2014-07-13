package org.jvalue.ceps.data;

import static org.junit.Assert.fail;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;


public final class DataManagerTest {

	private static final String DATA_NAME = "dummy";
	private static final JsonNode DATA_VALUE = new TextNode("value");

	private int onNewDataCount = 0;

	@Test
	public final void testDataListener() throws Exception {
		DataManager manager = DummyDataManager.createInstance();
		DataChangeListener listener1 = new DummyDataChangeListener();
		DataChangeListener listener2 = new DummyDataChangeListener();
		manager.onSourceChanged(DATA_NAME, DATA_VALUE);

		assertEquals(0, onNewDataCount);

		manager.registerDataListener(listener1);
		manager.registerDataListener(listener2);
		manager.onSourceChanged(DATA_NAME, DATA_VALUE);

		assertEquals(2, onNewDataCount);

		manager.unregisterDataListener(listener1);
		manager.onSourceChanged(DATA_NAME, DATA_VALUE);
		
		assertEquals(3, onNewDataCount);

		manager.unregisterDataListener(listener2);
		manager.onSourceChanged(DATA_NAME, DATA_VALUE);

		assertEquals(3, onNewDataCount);
	}


	private class DummyDataChangeListener implements DataChangeListener {

		@Override
		public void onNewDataType(String name, JsonNode shema) {
			fail();
		}

		@Override
		public void onNewData(String name, JsonNode data) {
			assertEquals(DATA_NAME, name);
			assertEquals(DATA_VALUE, data);
			onNewDataCount++;
		}

	}

}
