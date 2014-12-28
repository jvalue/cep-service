package org.jvalue.ceps.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.ektorp.support.CouchDbDocument;
import org.jvalue.ceps.ods.OdsClient;
import org.jvalue.ceps.ods.OdsDataSource;


/**
 * Combination of ODS source and client data to e.g. store in DB.
 */
public final class OdsRegistration extends CouchDbDocument {

	private final OdsClient client;
	private final OdsDataSource dataSource;

	@JsonCreator
	public OdsRegistration(
			@JsonProperty("dataSource") OdsDataSource dataSource,
			@JsonProperty("client") OdsClient client) {

		this.client = client;
		this.dataSource = dataSource;
	}


	public OdsDataSource getDataSource() {
		return dataSource;
	}


	public OdsClient getClient() {
		return client;
	}

}
