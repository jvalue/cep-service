package org.jvalue.ceps.api.notifications;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.Map;

public final class GcmClientDescription extends ClientDescription {

	@JsonCreator
	public GcmClientDescription(
			@JsonProperty("deviceId") String deviceId,
			@JsonProperty("eplArguments") Map<String, JsonNode> eplArguments) {

		super(GcmClient.CLIENT_TYPE, deviceId, eplArguments);
	}

	@Override
	public <P,R> R accept(ClientDescriptionVisitor<P,R> visitor, P param) {
		return visitor.visit(this, param);
	}

}
