package org.jvalue.ceps.notifications.clients;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


public final class DummyClient extends Client {

	@JsonCreator
	public DummyClient(
			@JsonProperty("clientId") String clientId,
			@JsonProperty("deviceId") String deviceId,
			@JsonProperty("eplStmt") String eplStmt) {

		super(clientId, deviceId, eplStmt);
	}

	
	@Override
	@SuppressWarnings("unchecked")
	public <P,R> R accept(ClientVisitor<P,R> visitor, P param) {
		if (visitor instanceof DeviceIdUpdater) {
			return (R) new DummyClient(getClientId(), param.toString(), getEplStmt());
		}
		throw new UnsupportedOperationException("stub");
	}

}
