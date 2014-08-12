package org.jvalue.ceps.notifications.sender; 

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.Test;
import org.jvalue.ceps.notifications.clients.DummyClient;

import com.fasterxml.jackson.databind.JsonNode;


public final class NotificationSenderTest {

	@Test
	public final void testSuccessResult() {

		DummyNotificationSender sender = new DummyNotificationSender();

		SenderResult success = sender.getSuccessResult();
		assertNotNull(success);
		assertEquals(success.getStatus(), SenderResult.Status.SUCCESS);
		assertNull(success.getRemoveDeviceId());
		assertNull(success.getUpdateDeviceId());
		assertNull(success.getErrorMsg());
		assertNull(success.getErrorCause());

	}


	@Test
	public final void testErrorMsgResult() {

		DummyNotificationSender sender = new DummyNotificationSender();

		SenderResult error = sender.getErrorResult("error");
		assertNotNull(error);
		assertEquals(error.getStatus(), SenderResult.Status.ERROR);
		assertEquals(error.getErrorMsg(), "error");
		assertNull(error.getRemoveDeviceId());
		assertNull(error.getUpdateDeviceId());
		assertNull(error.getErrorCause());

	}


	@Test
	public final void testErrorCauseResult() {

		DummyNotificationSender sender = new DummyNotificationSender();
		Exception exception = new RuntimeException("error");

		SenderResult error = sender.getErrorResult(exception);
		assertNotNull(error);
		assertEquals(error.getStatus(), SenderResult.Status.ERROR);
		assertEquals(error.getErrorCause(), exception);
		assertNull(error.getRemoveDeviceId());
		assertNull(error.getUpdateDeviceId());
		assertNull(error.getErrorMsg());

	}


	@Test
	public final void testClientResults() {

		DummyNotificationSender sender = new DummyNotificationSender();

		String oldDeviceId = "idOld";
		String newDeviceId = "idNew";

		SenderResult result = sender.getRemoveClientResult(oldDeviceId);
		assertNotNull(result);
		assertEquals(SenderResult.Status.REMOVE_CLIENT, result.getStatus());
		assertEquals(oldDeviceId, result.getRemoveDeviceId());
		assertNull(result.getUpdateDeviceId());
		assertNull(result.getErrorCause());
		assertNull(result.getErrorMsg());

		result = sender.getUpdateClientResult(oldDeviceId, newDeviceId);
		assertNotNull(result);
		assertEquals(SenderResult.Status.UPDATE_CLIENT, result.getStatus());
		assertNull(result.getRemoveDeviceId());
		assertEquals(oldDeviceId, result.getUpdateDeviceId().first);
		assertEquals(newDeviceId, result.getUpdateDeviceId().second);
		assertNull(result.getErrorCause());
		assertNull(result.getErrorMsg());

	}


	private static final class DummyNotificationSender extends NotificationSender<DummyClient> {

		@Override
		public SenderResult sendEventUpdate(
				DummyClient client,
				String eventId,
				List<JsonNode> newEvents,
				List<JsonNode> oldEvent) {

			throw new UnsupportedOperationException("not supported");
		}

	}

}
