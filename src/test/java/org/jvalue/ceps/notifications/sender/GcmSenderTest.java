package org.jvalue.ceps.notifications.sender;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.jvalue.ceps.notifications.clients.GcmClient;
import org.jvalue.ceps.notifications.utils.GcmUtils;
import org.mockito.ArgumentCaptor;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.fasterxml.jackson.databind.JsonNode;


@RunWith(PowerMockRunner.class)
@PrepareForTest({GcmUtils.class, GcmClient.class, GcmUtils.GcmResult.class})
public final class GcmSenderTest {


	@Test
	@SuppressWarnings({"unchecked", "rawtypes"})
	public final void testPayload() {
		GcmUtils utils = PowerMockito.mock(GcmUtils.class);
		GcmUtils.GcmResult result = getResult(true, null, false, null, null);
		when(utils.sendMsg(any(String.class), any(Map.class))).thenReturn(result);

		GcmSender sender = new GcmSender(utils);
		String EVENT_ID = "someEventId";

		SenderResult senderResult = sender.sendEventUpdate(
				getGcmClient(), 
				EVENT_ID, 
				new LinkedList<JsonNode>(), 
				new LinkedList<JsonNode>());
		assertEquals(SenderResult.Status.SUCCESS, senderResult.getStatus());

		ArgumentCaptor<Map> captor = ArgumentCaptor.forClass(Map.class);
		verify(utils).sendMsg(eq(DEVICE_ID), captor.capture());

		assertEquals(CLIENT_ID, captor.getValue().get(GcmSender.DATA_KEY_CLIENT_ID));
		assertEquals(EVENT_ID, captor.getValue().get(GcmSender.DATA_KEY_EVENT_ID));
	}


	@Test
	public final void testErrorResult() {
		String errorMsg = "some fail";
		SenderResult result = testResult(getResult(false, errorMsg, false, null, null));
		assertEquals(SenderResult.Status.ERROR, result.getStatus());
		assertEquals(errorMsg, result.getErrorMsg());

		IOException exception = new IOException("booom");
		result = testResult(getResult(false, null, false, null, exception));
		assertEquals(SenderResult.Status.ERROR, result.getStatus());
		assertEquals(exception, result.getErrorCause());
	}


	@Test
	public final void testUpdateResult() {
		String newId = "someNewGcmId";
		SenderResult result = testResult(getResult(false, null, false, newId, null));
		assertEquals(SenderResult.Status.UPDATE_CLIENT, result.getStatus());
		assertEquals(DEVICE_ID, result.getUpdateDeviceId().first);
		assertEquals(newId, result.getUpdateDeviceId().second);
	}


	@Test
	public final void testRemoveResult() {
		SenderResult result = testResult(getResult(false, null, true, null, null));
		assertEquals(SenderResult.Status.REMOVE_CLIENT, result.getStatus());
		assertEquals(DEVICE_ID, result.getRemoveDeviceId());
	}


	@SuppressWarnings("unchecked")
	private SenderResult testResult(GcmUtils.GcmResult gcmResult) {
		GcmUtils utils = PowerMockito.mock(GcmUtils.class);
		when(utils.sendMsg(any(String.class), any(Map.class))).thenReturn(gcmResult);

		return new GcmSender(utils).sendEventUpdate(
				getGcmClient(), 
				"someEventId", 
				new LinkedList<JsonNode>(), 
				new LinkedList<JsonNode>());
	}


	public GcmUtils.GcmResult getResult(
			boolean success,
			String errorMsg,
			boolean notRegistered,
			String newGcmId,
			IOException exception) {

		GcmUtils.GcmResult result = PowerMockito.mock(GcmUtils.GcmResult.class);
		when(result.isSuccess()).thenReturn(success);
		when(result.getErrorMsg()).thenReturn(errorMsg);
		when(result.isNotRegistered()).thenReturn(notRegistered);
		when(result.getNewGcmId()).thenReturn(newGcmId);
		when(result.getException()).thenReturn(exception);
		return result;
	}


	private static final String 
		CLIENT_ID = "someClientId",
		DEVICE_ID = "someDeviceId";

	private GcmClient getGcmClient() {
		GcmClient client = PowerMockito.mock(GcmClient.class);
		when(client.getClientId()).thenReturn(CLIENT_ID);
		when(client.getDeviceId()).thenReturn(DEVICE_ID);
		return client;
	}

}
