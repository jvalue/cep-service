package org.jvalue.ceps.notifications.sender;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;

import org.junit.Test;
import org.jvalue.ceps.notifications.clients.ClientFactory;
import org.jvalue.ceps.notifications.clients.GcmClient;

import com.fasterxml.jackson.databind.JsonNode;


public final class GcmSenderTest {

	@Test
	public final void testFail() {
		
		GcmSender sender = new GcmSender(GcmApiKeyHelper.getResourceName());
		GcmClient client = ClientFactory.createGcmClient("dummy", "dummy");

		try {
			SenderResult result = sender.sendEventUpdate(
					client,
					"dummy", 
					new LinkedList<JsonNode>(), 
					new LinkedList<JsonNode>());

			assertNotNull(result);
			assertEquals(result.getStatus(), SenderResult.Status.ERROR);
			assertTrue(result.getErrorMsg() != null || result.getErrorCause() != null);

		} catch (IllegalArgumentException iae) {
			// sender does not work when apikey is not set
			assertFalse(GcmApiKeyHelper.isApiKeyPresent());
		}

	}

}
