package org.jvalue.ceps.api.notifications;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.google.common.base.Objects;

import java.util.Map;


@JsonTypeInfo(
		use = Id.NAME,
		include = As.PROPERTY,
		property = "type",
		visible = true
)
@JsonSubTypes({
		@JsonSubTypes.Type(value = HttpClient.class, name = HttpClient.CLIENT_TYPE),
		@JsonSubTypes.Type(value = GcmClient.class, name = GcmClient.CLIENT_TYPE)
})
public abstract class Client {

	private final String type;
	/** Identifies one epl stmt. Must be unique across all stmts. */
	private final String id;
	/** Identifies one device. Multiple epl stmts can be mapped to one deviceId. */
	private final String deviceId;
	private final String eplAdapterId;
	private final Map<String, Object> eplArguments;
	private final String userId;

	Client(
			String type,
			String id,
			String deviceId,
			String eplAdapterId,
			Map<String, Object> eplArguments,
			String userId) {

		this.type = type;
		this.id = id;
		this.deviceId = deviceId;
		this.eplAdapterId = eplAdapterId;
		this.eplArguments = eplArguments;
		this.userId = userId;
	}


	public String getType() {
		return type;
	}


	public String getId() {
		return id;
	}


	public String getDeviceId() {
		return deviceId;
	}


	public String getEplAdapterId() {
		return eplAdapterId;
	}


	public Map<String, Object> getEplArguments() {
		return eplArguments;
	}


	public String getUserId() {
		return userId;
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Client client = (Client) o;
		return Objects.equal(type, client.type) &&
				Objects.equal(id, client.id) &&
				Objects.equal(deviceId, client.deviceId) &&
				Objects.equal(eplAdapterId, client.eplAdapterId) &&
				Objects.equal(eplArguments, client.eplArguments) &&
				Objects.equal(userId, client.userId);
	}


	@Override
	public int hashCode() {
		return Objects.hashCode(type, id, deviceId, eplAdapterId, eplArguments, userId);
	}


	public abstract <P,R> R accept(ClientVisitor<P,R> visitor, P param);

}
