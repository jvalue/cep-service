package org.jvalue.ceps.client;

import org.jvalue.ceps.utils.Assert;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
final class Client {

	private final String clientId, stmtId;

	@JsonCreator
	public Client(
			@JsonProperty("clientId") String clientId,
			@JsonProperty("stmtId") String stmtId) {

		Assert.assertNotNull(clientId, stmtId);
		this.clientId = clientId;
		this.stmtId = stmtId;
	}


	public String getClientId() {
		return clientId;
	}


	public String getStmtId() {
		return stmtId;
	}


	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof Client)) return false;
		if (other == this) return true;
		Client client = (Client) other;
		return clientId.equals(client.clientId) && stmtId.equals(client.stmtId);
	}


	@Override
	public int hashCode() {
		final int MULT = 17;
		int hash = 13;
		hash = hash + MULT * clientId.hashCode();
		hash = hash + MULT * stmtId.hashCode();
		return hash;
	}

}
