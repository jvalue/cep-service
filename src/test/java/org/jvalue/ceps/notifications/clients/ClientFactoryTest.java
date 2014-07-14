package org.jvalue.ceps.notifications.clients;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public final class ClientFactoryTest {

	@Test
	public void testGcmClient() {
		GcmClient client = ClientFactory.createGcmClient("dummy1", "dummy2");
		assertNotNull(client.getClientId());
		assertEquals("dummy1", client.getEplStmt());
		assertEquals("dummy2", client.getGcmId());
	}

}
