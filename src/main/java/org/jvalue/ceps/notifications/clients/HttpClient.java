package org.jvalue.ceps.notifications.clients;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


public final class HttpClient extends Client {

	/**
	 * The deviceId represents the client's url.
	 */
	@JsonCreator
	public HttpClient(
			@JsonProperty("id") String id,
			@JsonProperty("deviceId") String clientCallbackUrl,
			@JsonProperty("eplStmt") String eplStmt) {

		super(id, clientCallbackUrl, eplStmt);
	}


	@Override
	public <P,R> R accept(ClientVisitor<P,R> visitor, P param) {
		return visitor.visit(this, param);
	}

}
