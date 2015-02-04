package org.jvalue.ceps.db;


import com.fasterxml.jackson.databind.JsonNode;

import org.ektorp.CouchDbInstance;
import org.junit.Assert;
import org.junit.Test;
import org.jvalue.ceps.event.Event;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public final class EventRepositoryTest extends AbstractRepositoryTest_Old<EventRepository, Event> {

	private static final String
			EVENT_ID_1 = "eventId1",
			EVENT_ID_2 = "eventId2";


	public EventRepositoryTest() {
		super(EventRepositoryTest.class.getSimpleName());
	}


	@Test
	public void testFindByEventId() {
		Map<String, Event> events = doSetupDataItems();
		for (Event event : events.values()) repository.add(event);

		for (Map.Entry<String, Event> entry : events.entrySet()) {
			Event event = repository.findByEventId(entry.getKey());
			Assert.assertEquals(entry.getValue(), event);
		}
	}


	@Override
	protected EventRepository doCreateDatabase(CouchDbInstance couchDbInstance, String databaseName) {
		return new EventRepository(couchDbInstance.createConnector(databaseName, true));
	}


	@Override
	protected Map<String, Event> doSetupDataItems() {
		Map<String, Event> events = new HashMap<>();
		events.put(EVENT_ID_1, new Event(EVENT_ID_1, 1234, new LinkedList<JsonNode>(), new LinkedList<JsonNode>()));
		events.put(EVENT_ID_2, new Event(EVENT_ID_2, 5678, new LinkedList<JsonNode>(), new LinkedList<JsonNode>()));
		return events;
	}


	@Override
	protected String doGetIdForItem(Event client) {
		return client.getEventId();
	}

}
