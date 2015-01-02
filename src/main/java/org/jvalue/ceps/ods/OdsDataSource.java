package org.jvalue.ceps.ods;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class OdsDataSource extends OdsDataSourceDescription {

	private final String id;

	@JsonCreator
	public OdsDataSource(
			@JsonProperty("id") String id,
			@JsonProperty("schema") JsonNode schema,
			@JsonProperty("domainIdKey") String domainIdKey,
			@JsonProperty("metaData") OdsDataSourceMetaData metaData) {

		super(schema, domainIdKey, metaData);
		this.id = id;
	}

	public String getId() {
		return id;
	}

}
