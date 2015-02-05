package org.jvalue.ceps.api.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Objects;

import java.util.List;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;


public final class Event {

	@NotNull private final String id;
	@DecimalMin("0") private final long timestamp;
	@NotNull private final List<JsonNode> newEventData;
	@NotNull private final List<JsonNode> oldEventData;

	@JsonCreator
	public Event(
			@JsonProperty("id") String id,
			@JsonProperty("timestamp") long timestamp,
			@JsonProperty("newEventData") List<JsonNode> newEventData,
			@JsonProperty("oldEventData") List<JsonNode> oldEventData) {

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
