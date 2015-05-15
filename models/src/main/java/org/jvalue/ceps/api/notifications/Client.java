package org.jvalue.ceps.api.notifications;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.google.common.base.Objects;


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
	private final String eplStmt;
	private final String userId;

	Client(
			String type,
			String id,
			String deviceId,
			String eplStmt,
			String userId) {

		this.type = type;
		this.id = id;
		this.deviceId = deviceId;
		this.eplStmt = eplStmt;
		this.userId = userId;
	}


	public String getType() {
		return type;
	}


	public final String getId() {
		return id;
	}


	public final String getDeviceId() {
		return deviceId;
	}


	public final String getEplStmt() {
		return eplStmt;
	}


	public final String getUserId() {
		return userId;
	}


	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof Client)) return false;
		if (other == this) return true;
		Client client = (Client) other;
		return Objects.equal(id, client.id)
				&& Objects.equal(deviceId, client.deviceId)
				&& Objects.equal(eplStmt, client.eplStmt)
				&& Objects.equal(userId, client.userId);
	}


	@Override
	public int hashCode() {
		return Objects.hashCode(id, deviceId, eplStmt, userId);
	}


	public abstract <P,R> R accept(ClientVisitor<P,R> visitor, P param);

}
