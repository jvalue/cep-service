package org.jvalue.ceps.api.notifications;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.Map;

import javax.validation.constraints.NotNull;


@JsonTypeInfo(
		use = JsonTypeInfo.Id.NAME,
		include = JsonTypeInfo.As.PROPERTY,
		property = "type",
		visible = true
)
@JsonSubTypes({
		@JsonSubTypes.Type(value = HttpClientDescription.class, name = HttpClient.CLIENT_TYPE),
		@JsonSubTypes.Type(value = GcmClientDescription.class, name = GcmClient.CLIENT_TYPE)
})
public abstract class ClientDescription {

	@NotNull private final String type;
	@NotNull private final String deviceId;
	@NotNull private final Map<String, JsonNode> eplArguments;

	public ClientDescription(String type, String deviceId, Map<String, JsonNode> eplArguments) {
		this.type = type;
		this.deviceId = deviceId;
		this.eplArguments = eplArguments;
	}


	public String getType() {
		return type;
	}


	public String getDeviceId() {
		return deviceId;
	}


	public Map<String, JsonNode> getEplArguments() {
		return eplArguments;
	}


	public abstract <P,R> R accept(ClientDescriptionVisitor<P,R> visitor, P param);

}
