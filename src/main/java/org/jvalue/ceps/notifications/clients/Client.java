package org.jvalue.ceps.notifications.clients;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.google.common.base.Objects;

import org.jvalue.ceps.utils.Assert;


@JsonTypeInfo(use=Id.CLASS, include=As.PROPERTY, property="class")
public abstract class Client {

	/** Identifies one epl stmt. Must be unique across all stmts. */
	private final String id;
	/** Identifies one device. Mutliple epl stmts can be mapped to one deviceId. */
	private final String deviceId;
	private final String eplStmt;

	Client(
			String id,
			String deviceId,
			String eplStmt) {

		Assert.assertNotNull(id, deviceId, eplStmt);
		this.id = id;
		this.deviceId = deviceId;
		this.eplStmt = eplStmt;
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
