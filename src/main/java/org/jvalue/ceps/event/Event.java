package org.jvalue.ceps.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Objects;

import org.jvalue.ceps.utils.Assert;

import java.util.List;


public final class Event {

	private final String id;
	private final long timestamp;
	private final List<JsonNode> newEventData;
	private final List<JsonNode> oldEventData;

	@JsonCreator
	public Event(
			@JsonProperty("id") String id,
			@JsonProperty("timestamp") long timestamp,
			@JsonProperty("newEventData") List<JsonNode> newEventData,
			@JsonProperty("oldEventData") List<JsonNode> oldEventData) {

		Assert.assertNotNull(id, newEventData, oldEventData);;
		this.id = id;
		this.timestamp = timestamp;
		this.newEventData = newEventData;
		this.oldEventData = oldEventData;
	}


	public String getId() {
		return id;
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
		return Objects.equal(id, event.id)
				&& Objects.equal(timestamp, event.timestamp)
				&& Objects.equal(newEventData, event.newEventData)
				&& Objects.equal(oldEventData, event.oldEventData);
	}


	@Override
	public int hashCode() {
		return Objects.hashCode(id, timestamp, newEventData, oldEventData);
	}

}
