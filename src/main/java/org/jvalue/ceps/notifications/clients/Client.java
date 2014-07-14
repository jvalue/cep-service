package org.jvalue.ceps.notifications.clients;

import org.jvalue.ceps.utils.Assert;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use=Id.CLASS, include=As.PROPERTY, property="class")
public abstract class Client {

	private final String clientId;
	private final String eplStmt;

	Client(String clientId, String eplStmt) {
		Assert.assertNotNull(clientId, eplStmt);
		this.clientId = clientId;
		this.eplStmt = eplStmt;
	}


	public final String getClientId() {
		return clientId;
	}


	public final String getEplStmt() {
		return eplStmt;
	}


	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof Client)) return false;
		if (other == this) return true;
		Client client = (Client) other;
		return clientId.equals(client.clientId) && eplStmt.equals(client.eplStmt);
	}


	final int MULT = 17;

	@Override
	public int hashCode() {
		int hash = 13;
		hash = hash + MULT * clientId.hashCode();
		hash = hash + MULT * eplStmt.hashCode();
		return hash;
	}

}
