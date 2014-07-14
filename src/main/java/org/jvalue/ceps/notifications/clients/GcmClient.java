package org.jvalue.ceps.notifications.clients;

import org.jvalue.ceps.utils.Assert;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


public final class GcmClient extends Client {

	private final String gcmId;

	@JsonCreator
	GcmClient(
			@JsonProperty("clientId") String clientId,
			@JsonProperty("eplStmt") String eplStmt,
			@JsonProperty("gcmId") String gcmId) {

		super(clientId, eplStmt);
		Assert.assertNotNull(gcmId);
		this.gcmId = gcmId;
	}


	public String getGcmId() {
		return gcmId;
	}


	@Override
	public boolean equals(Object other) {
		if (!super.equals(other)) return false;
		if (other == this) return true;
		if (!(other instanceof GcmClient)) return false;
		GcmClient client = (GcmClient) other;
		return gcmId.equals(client.gcmId);
	}


	@Override
	public int hashCode() {
		int hash = super.hashCode();
		hash = hash + MULT * gcmId.hashCode();
		return hash;
	}

}
