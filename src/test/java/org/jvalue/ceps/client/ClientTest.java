package org.jvalue.ceps.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class ClientTest {

	private static final ObjectMapper mapper = new ObjectMapper();


	@Test
	public void testGet() {
		Client client = new Client("dummy1", "dummy2");
		assertEquals("dummy1", client.getClientId());
		assertEquals("dummy2", client.getStmtId());
	}


	@Test
	public void testEqualsAndHashCode() {
		Client client1 = new Client("dummy", "dummy");
		Client client2 = new Client("dummy", "dummy");
		Client client3 = new Client("dummy2", "dummy");
		Client client4 = new Client("dummy", "dummy2");
		Client client5 = new Client("dummy2", "dummy2");

		assertEquals(client1, client2);
		assertNotEquals(client1, client3);
		assertNotEquals(client1, client4);
		assertNotEquals(client1, client5);

		assertEquals(client1.hashCode(), client2.hashCode());
		assertNotEquals(client1.hashCode(), client3.hashCode());
		assertNotEquals(client1.hashCode(), client4.hashCode());
		assertNotEquals(client1.hashCode(), client5.hashCode());
	}


	@Test
	public void testJson() throws JsonProcessingException {
		Client client = new Client("dummy1", "dummy2");
		JsonNode json = mapper.valueToTree(client);
		assertNotNull(json);
		assertEquals(client, mapper.treeToValue(json, Client.class));
	}

}
