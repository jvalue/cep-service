package org.jvalue.ceps.notifications.clients;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public final class ClientTest {

	@Test
	public void testGet() {
		Client client = newClient("dummy1", "dummy2", "dummy3");
		assertEquals("dummy1", client.getClientId());
		assertEquals("dummy2", client.getDeviceId());
		assertEquals("dummy3", client.getEplStmt());
	}


	@Test
	public void testEqualsAndHashCode() {
		Client client1 = newClient("dummy", "dummy", "dummy");
		Client client2 = newClient("dummy", "dummy", "dummy");
		Client client3 = newClient("dummy2", "dummy", "dummy");
		Client client4 = newClient("dummy", "dummy2", "dummy");
		Client client5 = newClient("dummy", "dummy", "dummy2");

		assertEquals(client1, client2);
		assertNotEquals(client1, client3);
		assertNotEquals(client1, client4);
		assertNotEquals(client1, client5);

		assertEquals(client1.hashCode(), client2.hashCode());
		assertNotEquals(client1.hashCode(), client3.hashCode());
		assertNotEquals(client1.hashCode(), client4.hashCode());
		assertNotEquals(client1.hashCode(), client5.hashCode());
	}


	private Client newClient(String clientId, String deviceId, String eplStmt) {
		return new DummyClient(clientId, deviceId, eplStmt);
	}

}
