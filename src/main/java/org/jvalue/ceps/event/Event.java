package org.jvalue.ceps.event;

import java.util.List;

import org.jvalue.ceps.utils.Assert;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;


@JsonIgnoreProperties(ignoreUnknown = true)
public final class Event {

	private final String eventId;
	private final List<JsonNode> newEventData;
	private final List<JsonNode> oldEventData;

	@JsonCreator
	public Event(
			@JsonProperty("eventId") String eventId,
			@JsonProperty("newEventData") List<JsonNode> newEventData,
			@JsonProperty("oldEventData") List<JsonNode> oldEventData) {

		Assert.assertNotNull(eventId, newEventData, oldEventData);;
		this.eventId = eventId;
		this.newEventData = newEventData;
		this.oldEventData = oldEventData;
	}


	public String getEventId() {
		return eventId;
	}


	public List<JsonNode> getNewEventData() {
		return newEventData;
	}


	public List<JsonNode> getOldEventData() {
		return oldEventData;
	}


	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof Event)) return false;
		if (other == this) return true;
		Event event = (Event) other;
		return eventId.equals(event.eventId)
			&& newEventData.equals(event.newEventData)
			&& oldEventData.equals(event.oldEventData);
	}


	@Override
	public int hashCode() {
		final int MULT = 17;
		int hash = 13;
		hash = hash + MULT * eventId.hashCode();
		hash = hash + MULT * newEventData.hashCode();
		hash = hash + MULT * oldEventData.hashCode();
		return hash;
	}

}
