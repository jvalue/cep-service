package org.jvalue.ceps.event;

import com.fasterxml.jackson.databind.JsonNode;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jvalue.ceps.api.event.Event;

import java.util.LinkedList;
import java.util.List;

import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;


@RunWith(JMockit.class)
public final class EventGarbageCollectorTest {

	@Mocked private EventManager eventManager;


	@Test
	public void testCleaning() throws Exception {
		new Expectations() {{
			List<Event> events = new LinkedList<>();
			events.add(new Event("someId1", 0, new LinkedList<JsonNode>(), new LinkedList<JsonNode>()));
			events.add(new Event("someId2", 0, new LinkedList<JsonNode>(), new LinkedList<JsonNode>()));
			events.add(new Event("someId3", System.currentTimeMillis() + 500, new LinkedList<JsonNode>(), new LinkedList<JsonNode>()));

			eventManager.getAll();
			result = events;
		}};

		EventGarbageCollector collector = new EventGarbageCollector(eventManager, 200, 200);
		collector.start();
		Thread.sleep(150);
		collector.stop();
		Thread.sleep(150);

		new Verifications() {{
			List<String> removedEventIds = new LinkedList<>();
			eventManager.remove(withCapture(removedEventIds));
			times = 2;

			Assert.assertTrue(removedEventIds.contains("someId1"));
			Assert.assertTrue(removedEventIds.contains("someId2"));
		}};
	}

}
