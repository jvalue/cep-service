package org.jvalue.ceps.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Objects;

import org.ektorp.support.CouchDbDocument;
import org.jvalue.ceps.utils.Assert;

import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public final class Event extends CouchDbDocument {

	private final String eventId;
	private final long timestamp;
	private final List<JsonNode> newEventData;
	private final List<JsonNode> oldEventData;

	@JsonCreator
	public Event(
			@JsonProperty("eventId") String eventId,
			@JsonProperty("timestamp") long timestamp,
			@JsonProperty("newEventData") List<JsonNode> newEventData,
			@JsonProperty("oldEventData") List<JsonNode> oldEventData) {

		Assert.assertNotNull(eventId, newEventData, oldEventData);;
		this.eventId = eventId;
		this.timestamp = timestamp;
		this.newEventData = newEventData;
		this.oldEventData = oldEventData;
	}


	public String getEventId() {
		return eventId;
	}


	public long getTimestamp() {
		return timestamp;
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
		return Objects.equal(eventId, event.eventId)
				&& Objects.equal(timestamp, event.timestamp)
				&& Objects.equal(newEventData, event.newEventData)
				&& Objects.equal(oldEventData, event.oldEventData);
	}


	@Override
	public int hashCode() {
		return Objects.hashCode(eventId, timestamp, newEventData, oldEventData);
	}

}
