package org.jvalue.ceps.notifications.clients;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


public final class GcmClient extends Client {

	/**
	 * THe deviceId is represented through the gcm id.
	 */
	@JsonCreator
	GcmClient(
			@JsonProperty("clientId") String clientId,
			@JsonProperty("deviceId") String gcmId,
			@JsonProperty("eplStmt") String eplStmt) {

		super(clientId, gcmId, eplStmt);
	}

}
