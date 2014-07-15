package org.jvalue.ceps.event;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.jvalue.ceps.db.DbAccessorFactory;
import org.jvalue.ceps.db.JsonObjectDb;
import org.jvalue.ceps.utils.Assert;

import com.fasterxml.jackson.databind.JsonNode;


public final class EventManager {

	private static final String DB_NAME = "cepsEvents";

	private static EventManager instance;

	public static EventManager getInstance() {
		if (instance == null) {
			JsonObjectDb<Event> eventDb = new JsonObjectDb<Event>(
					DbAccessorFactory.getCouchDbAccessor(DB_NAME),
					Event.class);
			instance = new EventManager(eventDb);
		}
		return instance;
	}


	private JsonObjectDb<Event> eventDb;

	private EventManager(JsonObjectDb<Event> eventDb) {
		Assert.assertNotNull(eventDb);
		this.eventDb = eventDb;
	}


	public String onNewEvents(
			List<JsonNode> newEvents, 
			List<JsonNode> oldEvents) {

		String eventId = UUID.randomUUID().toString();
		Event event = new Event(eventId, newEvents, oldEvents);
		eventDb.add(event);
		return eventId;
	}


	public Event getEvent(String eventId) {
		Assert.assertNotNull(eventId);
		for (Event event : eventDb.getAll()) {
			if (event.getEventId().equals(eventId))
				return event;
		}
		return null;
	}


	public void removeEvent(String eventId) {
		Assert.assertNotNull(eventId);
		Event event = getEvent(eventId);
		eventDb.remove(event);
	}


	public List<Event> getAll() {
		return new LinkedList<Event>(eventDb.getAll());
	}


}
