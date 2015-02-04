package org.jvalue.ceps.notifications.clients;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public final class ClientTest {

	@Test
	public void testGet() {
		Client client = new DummyClient("dummy1", "dummy2", "dummy3");
		assertEquals("dummy1", client.getId());
		assertEquals("dummy2", client.getDeviceId());
		assertEquals("dummy3", client.getEplStmt());
	}


	@Test
	public void testEqualsAndHashCode() {
		Client client1 = new DummyClient("dummy", "dummy", "dummy");
		Client client2 = new DummyClient("dummy", "dummy", "dummy");
		Client client3 = new DummyClient("dummy2", "dummy", "dummy");
		Client client4 = new DummyClient("dummy", "dummy2", "dummy");
		Client client5 = new DummyClient("dummy", "dummy", "dummy2");

		assertEquals(client1, client2);
		assertNotEquals(client1, client3);
		assertNotEquals(client1, client4);
		assertNotEquals(client1, client5);

		assertEquals(client1.hashCode(), client2.hashCode());
		assertNotEquals(client1.hashCode(), client3.hashCode());
		assertNotEquals(client1.hashCode(), client4.hashCode());
		assertNotEquals(client1.hashCode(), client5.hashCode());
	}


	private static final class DummyClient extends Client {

		public DummyClient(String clientId, String deviceId, String eplStmt) {
			super(clientId, deviceId, eplStmt);
		}


		@Override
		public <P,R> R accept(ClientVisitor<P,R> visitor, P param) {
			return null;
		}

	}

}
