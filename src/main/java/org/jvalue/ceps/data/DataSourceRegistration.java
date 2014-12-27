package org.jvalue.ceps.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Objects;

import org.jvalue.ceps.utils.Assert;


@JsonIgnoreProperties(ignoreUnknown = true)
public final class DataSourceRegistration {

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
		return Objects.equal(clientId, registration.clientId)
				&& Objects.equal(dataSource, registration.dataSource)
				&& Objects.equal(dataSchema, registration.dataSchema);
	}


	@Override
	public int hashCode() {
		return Objects.hashCode(clientId, dataSource, dataSchema);
	}

}
