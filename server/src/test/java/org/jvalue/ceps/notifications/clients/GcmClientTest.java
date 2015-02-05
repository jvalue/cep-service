package org.jvalue.ceps.notifications.clients;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class GcmClientTest {

	private static final ObjectMapper mapper = new ObjectMapper();

	@Test
	public void testJson() throws JsonProcessingException {
		GcmClient client = new GcmClient("dummy1", "dummy2", "dummy3");
		JsonNode json = mapper.valueToTree(client);
		assertNotNull(json);
		assertEquals(client, mapper.treeToValue(json, Client.class));
	}

}
