package org.jvalue.ceps.notifications.clients;

import java.util.UUID;

import org.jvalue.ceps.utils.Assert;


public final class ClientFactory {

	private ClientFactory() { }


	public static GcmClient createGcmClient(String gcmId, String eplStmt) {
		Assert.assertNotNull(gcmId, eplStmt);
		return new GcmClient(UUID.randomUUID().toString(), gcmId, eplStmt);
	}

}
