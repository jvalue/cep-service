package org.jvalue.ceps.ods;

import com.fasterxml.jackson.databind.JsonNode;


public class OdsDataSourceDescription {

	private final JsonNode schema;
	private final String domainIdKey;
	private final OdsDataSourceMetaData metaData;

	public OdsDataSourceDescription(JsonNode schema, String domainIdKey, OdsDataSourceMetaData metaData) {
		this.schema = schema;
		this.domainIdKey = domainIdKey;
		this.metaData = metaData;
	}


	public JsonNode getSchema() {
		return schema;
	}


	public OdsDataSourceMetaData getMetaData() {
		return metaData;
	}


	public String getDomainIdKey() {
		return domainIdKey;
	}

}
