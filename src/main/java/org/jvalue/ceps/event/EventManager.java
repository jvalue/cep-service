package org.jvalue.ceps.event;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;

import org.jvalue.ceps.db.EventRepository;
import org.jvalue.ceps.utils.Assert;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;


public final class EventManager {

	private final EventRepository eventRepository;

	@Inject
	EventManager(EventRepository eventRepository) {
		this.eventRepository = eventRepository;
	}


	public String onNewEvents(
			List<JsonNode> newEvents, 
			List<JsonNode> oldEvents) {

		String eventId = UUID.randomUUID().toString();
		Event event = new Event(eventId, System.currentTimeMillis(), newEvents, oldEvents);
		eventRepository.add(event);
		return eventId;
	}


	public Event getEvent(String eventId) {
		Assert.assertNotNull(eventId);
		return eventRepository.findByEventId(eventId);
	}


	public void removeEvent(String eventId) {
		Assert.assertNotNull(eventId);
		Event event = eventRepository.findByEventId(eventId);
		eventRepository.remove(event);
	}


	public List<Event> getAll() {
		return new LinkedList<>(eventRepository.getAll());
	}


}
