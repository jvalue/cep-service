package org.jvalue.ceps.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.ektorp.support.CouchDbDocument;
import org.jvalue.ods.api.notifications.HttpClient;
import org.jvalue.ods.api.sources.DataSource;


/**
 * Combination of ODS source and client data to e.g. store in DB.
 */
public final class OdsRegistration extends CouchDbDocument {

	private final HttpClient client;
	private final DataSource dataSource;

	@JsonCreator
	public OdsRegistration(
			@JsonProperty("dataSource") DataSource dataSource,
			@JsonProperty("client") HttpClient client) {

		this.client = client;
		this.dataSource = dataSource;
	}


	public DataSource getDataSource() {
		return dataSource;
	}


	public HttpClient getClient() {
		return client;
	}

}
