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

		String CLIENT_ID = "someClientId";
		String DEVICE_ID = "someDeviceId";
		GcmClient client = PowerMockito.mock(GcmClient.class);
		when(client.getClientId()).thenReturn(CLIENT_ID);
		when(client.getDeviceId()).thenReturn(DEVICE_ID);

		SenderResult senderResult = sender.sendEventUpdate(client, EVENT_ID, new LinkedList<JsonNode>(), new LinkedList<JsonNode>());
		assertEquals(SenderResult.Status.SUCCESS, senderResult.getStatus());

		ArgumentCaptor<Map> captor = ArgumentCaptor.forClass(Map.class);
		verify(utils).sendMsg(eq(DEVICE_ID), captor.capture());

		assertEquals(CLIENT_ID, captor.getValue().get(GcmSender.DATA_KEY_CLIENT_ID));
		assertEquals(EVENT_ID, captor.getValue().get(GcmSender.DATA_KEY_EVENT_ID));
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

		

}
