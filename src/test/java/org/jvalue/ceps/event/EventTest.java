package org.jvalue.ceps.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TextNode;

public final class EventTest {

	private static final ObjectMapper mapper = new ObjectMapper();


	@Test
	public void testGet() {
		List<JsonNode> newData = createEventData("newDummy");
		List<JsonNode> oldData = createEventData("oldDummy");
		long timestamp = System.currentTimeMillis();
		Event event = new Event("dummy", timestamp, newData, oldData);
		assertEquals("dummy", event.getEventId());
		assertEquals(timestamp, event.getTimestamp());
		assertEquals(newData, event.getNewEventData());
		assertEquals(oldData, event.getOldEventData());
	}


	@Test
	public void testEqualsAndHashCode() {
		long timestamp = System.currentTimeMillis();
		Event event1 = new Event("dummy", timestamp, createEventData("newDummy"), createEventData("oldDummy"));
		Event event2 = new Event("dummy", timestamp, createEventData("newDummy"), createEventData("oldDummy"));
		Event event3 = new Event("dummy", timestamp, createEventData("dummy"), createEventData("oldDummy"));
		Event event4 = new Event("dummy", timestamp, createEventData("newDummy"), createEventData("dummy"));
		Event event5 = new Event("dummy2", System.currentTimeMillis(), createEventData("newDummy"), createEventData("oldDummy"));

		assertEquals(event1, event2);
		assertNotEquals(event1, event3);
		assertNotEquals(event1, event4);
		assertNotEquals(event1, event5);

		assertEquals(event1.hashCode(), event2.hashCode());
		assertNotEquals(event1.hashCode(), event3.hashCode());
		assertNotEquals(event1.hashCode(), event4.hashCode());
		assertNotEquals(event1.hashCode(), event5.hashCode());
	}


	@Test
	public void testJson() throws JsonProcessingException {
		Event event = new Event("dummy", System.currentTimeMillis(), createEventData("dummy1"), createEventData("dummy2"));
		JsonNode json = mapper.valueToTree(event);
		assertNotNull(json);
		assertEquals(event, mapper.treeToValue(json, Event.class));
	}


	private List<JsonNode> createEventData(String ... stringData) {
		List<JsonNode> data = new LinkedList<JsonNode>();
		for (String item : stringData) data.add(new TextNode(item));
		return data;
	}

}
