package org.jvalue.ceps.api.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.common.base.Objects;

import javax.validation.constraints.NotNull;


/**
 * Data which can be sent to the CEPS for processing.
 */
public final class SourceData {

	@NotNull private final String sourceId;
	@NotNull private final ArrayNode data;

	@JsonCreator
	public SourceData(
			@JsonProperty("sourceId") String sourceId,
			@JsonProperty("data") ArrayNode data) {

		this.sourceId = sourceId;
		this.data = data;
	}


	public String getSourceId() {
		return sourceId;
	}


	public ArrayNode getData() {
		return data;
	}


	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof SourceData)) return false;
		if (other == this) return true;
		SourceData sourceData = (SourceData) other;
		return Objects.equal(sourceId, sourceData.sourceId)
				&& Objects.equal(data, sourceData.data);
	}


	@Override
	public int hashCode() {
		return Objects.hashCode(sourceId, data);
	}

}
