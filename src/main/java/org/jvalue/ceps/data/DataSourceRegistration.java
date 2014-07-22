package org.jvalue.ceps.data;

import org.jvalue.ceps.utils.Assert;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;


@JsonIgnoreProperties(ignoreUnknown = true)
final class DataSourceRegistration {

	private final String clientId;
	private final DataSource dataSource;
	private final JsonNode dataSchema;

	@JsonCreator
	public DataSourceRegistration(
			@JsonProperty("clientId") String clientId, 
			@JsonProperty("dataSource") DataSource dataSource,
			@JsonProperty("dataSchema") JsonNode dataSchema) {

		Assert.assertNotNull(clientId, dataSource, dataSchema);
		this.clientId = clientId;
		this.dataSource = dataSource;
		this.dataSchema = dataSchema;
	}


	public String getClientId() {
		return clientId;
	}


	public DataSource getDataSource() {
		return dataSource;
	}


	public JsonNode getDataSchema() {
		return dataSchema;
	}



	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof DataSourceRegistration)) return false;
		DataSourceRegistration registration = (DataSourceRegistration) other;
		return registration.clientId.equals(clientId) 
			&& registration.dataSource.equals(dataSource)
			&& registration.dataSchema.equals(dataSchema);
	}


	@Override
	public int hashCode() {
		final int MULT = 17;
		int hash = 13;
		hash = hash + MULT * clientId.hashCode();
		hash = hash + MULT * dataSource.hashCode();
		hash = hash + MULT * dataSchema.hashCode();
		return hash;
	}

}
