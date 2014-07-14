package org.jvalue.ceps.notifications.clients;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class GcmClientTest {

	private static final ObjectMapper mapper = new ObjectMapper();

	@Test
	public void testGet() {
		GcmClient client = new GcmClient("dummy1", "dummy2", "dummy3");
		assertEquals("dummy1", client.getClientId());
		assertEquals("dummy2", client.getEplStmt());
		assertEquals("dummy3", client.getGcmId());
	}


	@Test
	public void testEqualsAndHashCode() {
		GcmClient client1 = new GcmClient("dummy", "dummy", "dummy");
		GcmClient client2 = new GcmClient("dummy", "dummy", "dummy");
		GcmClient client3 = new GcmClient("dummy", "dummy", "dummy2");

		assertEquals(client1, client2);
		assertNotEquals(client1, client3);

		assertEquals(client1.hashCode(), client2.hashCode());
		assertNotEquals(client1.hashCode(), client3.hashCode());
	}


	@Test
	public void testJson() throws JsonProcessingException {
		GcmClient client = new GcmClient("dummy1", "dummy2", "dummy3");
		JsonNode json = mapper.valueToTree(client);
		assertNotNull(json);
		assertEquals(client, mapper.treeToValue(json, Client.class));
	}

}
