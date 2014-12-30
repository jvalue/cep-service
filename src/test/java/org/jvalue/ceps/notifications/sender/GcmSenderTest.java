package org.jvalue.ceps.notifications.sender;

import com.fasterxml.jackson.databind.JsonNode;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jvalue.ceps.notifications.clients.GcmClient;
import org.jvalue.ceps.notifications.utils.GcmUtils;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Map;

import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;


@RunWith(JMockit.class)
public final class GcmSenderTest {

	private static final String
			CLIENT_ID = "clientId",
			DEVICE_ID = "deviceId",
			EVENT_ID = "eventId";

	private static final GcmClient client = new GcmClient(CLIENT_ID, DEVICE_ID, "eplStmt");


	@Mocked private GcmUtils gcmUtils;
	@Mocked private GcmUtils.GcmResult gcmResult;

	private GcmSender sender;


	@Before
	public void setupSender() {
		sender = new GcmSender(gcmUtils);

		new Expectations() {{
			gcmUtils.sendMsg(anyString, (Map) any);
			result = gcmResult;
		}};
	}


	@Test
	@SuppressWarnings("unchecked")
	public void testPayload() {
		new Expectations() {{
			gcmResult.isSuccess();
			result = true;
		}};

		SenderResult senderResult = sender.sendEventUpdate(client, EVENT_ID, new LinkedList<JsonNode>(), new LinkedList<JsonNode>());

		Assert.assertEquals(SenderResult.Status.SUCCESS, senderResult.getStatus());
		new Verifications() {{
			Map<String, String> payload;
			gcmUtils.sendMsg(DEVICE_ID, payload = withCapture());
			times = 1;

			Assert.assertEquals(EVENT_ID, payload.get("event"));
			Assert.assertEquals(CLIENT_ID, payload.get("client"));
		}};
	}


	@Test
	public void testErrorString() {
		String errorMsg = "some fail";
		SenderResult result = setupResult(false, errorMsg, false, null, null);

		Assert.assertEquals(SenderResult.Status.ERROR, result.getStatus());
		Assert.assertEquals(errorMsg, result.getErrorMsg());
	}


	@Test
	public void testErrorException() {
		IOException exception = new IOException("booom");
		SenderResult result = setupResult(false, null, false, null, exception);

		Assert.assertEquals(SenderResult.Status.ERROR, result.getStatus());
		Assert.assertEquals(exception, result.getErrorCause());
	}


	@Test
	public void testUpdateResult() {
		String newId = "someNewGcmId";
		SenderResult result = setupResult(false, null, false, newId, null);

		Assert.assertEquals(SenderResult.Status.UPDATE_CLIENT, result.getStatus());
		Assert.assertEquals(DEVICE_ID, result.getUpdateDeviceId().first);
		Assert.assertEquals(newId, result.getUpdateDeviceId().second);
	}


	@Test
	public void testRemoveResult() {
		SenderResult result = setupResult(false, null, true, null, null);

		Assert.assertEquals(SenderResult.Status.REMOVE_CLIENT, result.getStatus());
		Assert.assertEquals(DEVICE_ID, result.getRemoveDeviceId());
	}


	private SenderResult setupResult(
			final boolean success,
			final String errorMsg,
			final boolean notRegistered,
			final String newGcmId,
			final IOException exception) {

		new Expectations() {{
			gcmResult.isSuccess(); result = success; minTimes = 0;
			gcmResult.getErrorMsg(); result = errorMsg; minTimes = 0;
			gcmResult.isNotRegistered(); result = notRegistered; minTimes = 0;
			gcmResult.getNewGcmId(); result = newGcmId; minTimes = 0;
			gcmResult.getException(); returns(exception); minTimes = 0;
		}};

		return sender.sendEventUpdate(client, EVENT_ID, new LinkedList<JsonNode>(), new LinkedList<JsonNode>());
	}

}
