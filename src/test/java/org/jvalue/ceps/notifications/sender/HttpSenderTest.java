package org.jvalue.ceps.notifications.sender;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jvalue.ceps.notifications.clients.HttpClient;

import java.util.LinkedList;

import mockit.integration.junit4.JMockit;


@RunWith(JMockit.class)
public final class HttpSenderTest {

	private final String
			EVENT_ID = "someEventId",
			CLIENT_ID = "someClientId";

	private MockWebServer server;

	@Before
	public void startMockServer() throws Exception {
		server = new MockWebServer();
		server.enqueue(new MockResponse().setResponseCode(200));
		server.play();
	}


	@After
	public void stopMockServer() throws Exception {
		server.shutdown();
	}


	@Test
	public final void testSuccess() throws Throwable {
		String path = "/foo/bar/data/";
		String callbackUrl = server.getUrl(path).toString();
		HttpSender sender = new HttpSender();
		HttpClient client = new HttpClient(CLIENT_ID, callbackUrl, "someEplStmt");

		SenderResult result = sender.sendEventUpdate(client, EVENT_ID, new LinkedList<JsonNode>(), new LinkedList<JsonNode>());
		Assert.assertEquals(SenderResult.Status.SUCCESS, result.getStatus());

		ObjectNode sentData = new ObjectNode(JsonNodeFactory.instance);
		sentData.put("clientId", CLIENT_ID);
		sentData.put("eventId", EVENT_ID);
		RecordedRequest request = server.takeRequest();
		Assert.assertEquals(path, request.getPath());
		Assert.assertEquals(sentData.toString(), request.getUtf8Body());
	}

}
