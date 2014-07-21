package org.jvalue.ceps.data;

import org.jvalue.ceps.utils.Assert;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
public final class DataSourceRegistration {

	private final String clientId;
	private final DataSource dataSource;

	@JsonCreator
	public DataSourceRegistration(
			@JsonProperty("clientId") String clientId, 
			@JsonProperty("dataSource") DataSource dataSource) {

		Assert.assertNotNull(clientId, dataSource);
		this.clientId = clientId;
		this.dataSource = dataSource;
	}


	public String getClientId() {
		return clientId;
	}


	public DataSource getDataSource() {
		return dataSource;
	}



	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof DataSourceRegistration)) return false;
		DataSourceRegistration registration = (DataSourceRegistration) other;
		return registration.clientId.equals(clientId) 
			&& registration.dataSource.equals(dataSource);
	}


	@Override
	public int hashCode() {
		final int MULT = 17;
		int hash = 13;
		hash = hash + MULT * clientId.hashCode();
		hash = hash + MULT * dataSource.hashCode();
		return hash;
	}

}
