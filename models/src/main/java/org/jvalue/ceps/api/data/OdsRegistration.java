package org.jvalue.ceps.api.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

import org.jvalue.ods.api.notifications.HttpClient;
import org.jvalue.ods.api.sources.DataSource;


/**
 * Combination of ODS source and client data to e.g. store in DB.
 */
public final class OdsRegistration {

	private final String id;
	private final HttpClient client;
	private final DataSource dataSource;

	@JsonCreator
	public OdsRegistration(
			@JsonProperty("id") String id,
			@JsonProperty("dataSource") DataSource dataSource,
			@JsonProperty("client") HttpClient client) {

		this.id = id;
		this.client = client;
		this.dataSource = dataSource;
	}


	public String getId() {
		return id;
	}


	public DataSource getDataSource() {
		return dataSource;
	}


	public HttpClient getClient() {
		return client;
	}


	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof OdsRegistration)) return false;
		if (other == this) return true;
		OdsRegistration registration = (OdsRegistration) other;
		return Objects.equal(id, registration.id)
				&& Objects.equal(client, registration.client)
				&& Objects.equal(dataSource, registration.dataSource);
	}


	@Override
	public int hashCode() {
		return Objects.hashCode(id, client, dataSource);
	}

}
