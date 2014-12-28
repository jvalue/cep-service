package org.jvalue.ceps.event;

import java.util.LinkedList;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;

import static org.junit.Assert.assertEquals;


public final class EventClientGarbageCollectorTest {

	private static final long 
		MAX_AGE = 500,
		UPDATE_INTERVAL = 100;

	@Test
	public void testCleaning() throws Exception {
		EventManager manager = DummyEventManager.createInstance();
		manager.onNewEvents(new LinkedList<JsonNode>(), new LinkedList<JsonNode>());

		EventGarbageCollector collector = new EventGarbageCollector(manager, UPDATE_INTERVAL, MAX_AGE);
		collector.start();

		Thread.sleep(150);
		assertEquals(1, manager.getAll().size());

		Thread.sleep(150);
		assertEquals(1, manager.getAll().size());

		Thread.sleep(250);
		assertEquals(0, manager.getAll().size());

		collector.stop();

		manager.onNewEvents(new LinkedList<JsonNode>(), new LinkedList<JsonNode>());
		
		Thread.sleep(550);
		assertEquals(1, manager.getAll().size());
	}

}
