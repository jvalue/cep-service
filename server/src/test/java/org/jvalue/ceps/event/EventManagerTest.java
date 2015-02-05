package org.jvalue.ceps.event;

import com.fasterxml.jackson.databind.JsonNode;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jvalue.ceps.api.event.Event;
import org.jvalue.ceps.db.EventRepository;

import java.util.LinkedList;

import mockit.Mocked;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;

@RunWith(JMockit.class)
public final class EventManagerTest {

	@Mocked private EventRepository eventRepository;

	private EventManager eventManager;

	@Before
	public void setupEventManager() {
		this.eventManager = new EventManager(eventRepository);
	}


	@Test
	public void testOnNewEvents() {
		final String eventId = eventManager.onNewEvents(new LinkedList<JsonNode>(), new LinkedList<JsonNode>());

		Assert.assertNotNull(eventId);
		new Verifications() {{
			Event event;
			eventRepository.add(event = withCapture());
			Assert.assertEquals(eventId, event.getId());
			Assert.assertNotNull(event.getOldEventData());
			Assert.assertNotNull(event.getNewEventData());
			Assert.assertTrue(event.getTimestamp() > 0);
		}};
	}

}
