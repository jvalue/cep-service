package org.jvalue.ceps.notifications;

import org.jvalue.ceps.utils.Assert;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class Client {

	private final String clientId;
	private final String eplStmt;

	@JsonCreator
	public Client(
			@JsonProperty("clientId") String clientId,
			@JsonProperty("eplStmt") String eplStmt) {

		Assert.assertNotNull(clientId, eplStmt);
		this.clientId = clientId;
		this.eplStmt = eplStmt;
	}


	public String getClientId() {
		return clientId;
	}


	public String getEplStmt() {
		return eplStmt;
	}


	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof Client)) return false;
		if (other == this) return true;
		Client client = (Client) other;
		return clientId.equals(client.clientId) && eplStmt.equals(client.eplStmt);
	}


	@Override
	public int hashCode() {
		final int MULT = 17;
		int hash = 13;
		hash = hash + MULT * clientId.hashCode();
		hash = hash + MULT * eplStmt.hashCode();
		return hash;
	}

}
