package org.jvalue.ceps.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;

public final class EventManagerTest {

	@Test
	public void testCrud() throws Exception {
		EventManager manager = DummyEventManager.createInstance();

		assertTrue(manager.getAll().isEmpty());
		assertNull(manager.getEvent("dummy"));

		String eventId = manager.onNewEvents(new LinkedList<JsonNode>(), new LinkedList<JsonNode>());

		assertNotNull(eventId);
		assertNotNull(manager.getEvent(eventId));
		assertTrue(manager.getEvent(eventId).getOldEventData().isEmpty());
		assertTrue(manager.getEvent(eventId).getNewEventData().isEmpty());
		assertEquals(eventId, manager.getEvent(eventId).getEventId());
		assertTrue(manager.getEvent(eventId).getTimestamp() <= System.currentTimeMillis());
		assertTrue(manager.getAll().contains(manager.getEvent(eventId)));
		assertEquals(1, manager.getAll().size());

		manager.removeEvent(eventId);

		assertNull(manager.getEvent(eventId));
		assertTrue(manager.getAll().isEmpty());
	}

}
