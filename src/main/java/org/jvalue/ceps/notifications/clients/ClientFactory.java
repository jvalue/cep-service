package org.jvalue.ceps.notifications.clients;

import java.util.UUID;

import org.jvalue.ceps.utils.Assert;


public final class ClientFactory {

	private ClientFactory() { }


	public static GcmClient createGcmClient(String eplStmt, String gcmId) {
		Assert.assertNotNull(eplStmt, gcmId);
		return new GcmClient(UUID.randomUUID().toString(), gcmId, eplStmt);
	}


	public static GcmClient createGcmClient(GcmClient client, String newClientId) {
		Assert.assertNotNull(client, newClientId);
		return new GcmClient(newClientId, client.getDeviceId(), client.getEplStmt());
	}

}
