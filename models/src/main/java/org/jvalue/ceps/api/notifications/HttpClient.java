package org.jvalue.ceps.api.notifications;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;


public final class HttpClient extends Client {

	public static final String CLIENT_TYPE = "HTTP";

	/**
	 * The deviceId represents the client's url.
	 */
	@JsonCreator
	public HttpClient(
			@JsonProperty("id") String id,
			@JsonProperty("deviceId") String clientCallbackUrl,
			@JsonProperty("eplAdapterId") String eplAdapterId,
			@JsonProperty("eplArguments") Map<String, Object> eplArguments,
			@JsonProperty("clientId") String clientId) {

		super(CLIENT_TYPE, id, clientCallbackUrl, eplAdapterId, eplArguments, clientId);
	}


	@Override
	public <P,R> R accept(ClientVisitor<P,R> visitor, P param) {
		return visitor.visit(this, param);
	}

}
