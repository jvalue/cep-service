package org.jvalue.ceps.api.notifications;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;


public final class GcmClient extends Client {

	public static final String CLIENT_TYPE = "GCM";

	/**
	 * The deviceId is represented through the GCM id.
	 */
	@JsonCreator
	public GcmClient(
			@JsonProperty("id") String id,
			@JsonProperty("deviceId") String gcmId,
			@JsonProperty("eplAdapterId") String eplAdapterId,
			@JsonProperty("eplArguments") Map<String, Object> eplArguments,
			@JsonProperty("userId") String userId) {

		super(CLIENT_TYPE, id, gcmId, eplAdapterId, eplArguments, userId);
	}


	@Override
	public <P,R> R accept(ClientVisitor<P,R> visitor, P param) {
		return visitor.visit(this, param);
	}

}
