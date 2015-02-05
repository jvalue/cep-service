package org.jvalue.ceps.notifications.clients;

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

	Client(
			String type,
			String id,
			String deviceId,
			String eplStmt) {

		this.type = type;
		this.id = id;
		this.deviceId = deviceId;
		this.eplStmt = eplStmt;
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


	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof Client)) return false;
		if (other == this) return true;
		Client client = (Client) other;
		return Objects.equal(id, client.id)
				&& Objects.equal(deviceId, client.deviceId)
				&& Objects.equal(eplStmt, client.eplStmt);
	}


	@Override
	public int hashCode() {
		return Objects.hashCode(id, deviceId, eplStmt);
	}


	public abstract <P,R> R accept(ClientVisitor<P,R> visitor, P param);

}
