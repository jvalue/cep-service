package org.jvalue.ceps.ods;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class OdsDataSource {

	private String id;
	private JsonNode schema;

	public OdsDataSource() { }

	public OdsDataSource(String id, JsonNode schema) {
		this.id = id;
		this.schema = schema;
	}

	public String getId() {
		return id;
	}

	public JsonNode getSchema() {
		return schema;
	}

}
