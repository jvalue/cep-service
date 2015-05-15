package org.jvalue.ceps.notifications;

import org.junit.Assert;
import org.junit.Test;
import org.jvalue.ceps.api.notifications.Client;
import org.jvalue.ceps.api.notifications.GcmClient;

public final class DeviceIdUpdaterTest {

	private static final String
			CLIENT_ID = "clientId",
			DEVICE_ID_OLD = "oldDeviceId",
			DEVICE_ID_NEW = "newDeviceId",
			EPL_STMT = "eplStmt";

	@Test
	public void testGcmClientUpdate() {
		GcmClient oldClient = new GcmClient(CLIENT_ID, DEVICE_ID_OLD, EPL_STMT, "someUserId");
		Client newClient = oldClient.accept(new DeviceIdUpdater(), DEVICE_ID_NEW);

		Assert.assertEquals(CLIENT_ID, newClient.getId());
		Assert.assertEquals(DEVICE_ID_NEW, newClient.getDeviceId());
		Assert.assertEquals(EPL_STMT, newClient.getEplStmt());
	}

}
