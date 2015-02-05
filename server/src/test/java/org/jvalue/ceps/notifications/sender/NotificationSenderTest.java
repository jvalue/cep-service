package org.jvalue.ceps.notifications.sender;

import com.fasterxml.jackson.databind.JsonNode;

import org.junit.Assert;
import org.junit.Test;
import org.jvalue.ceps.api.notifications.Client;

import java.util.List;

public final class NotificationSenderTest {

	private final NotificationSender<Client> sender = new DummyNotificationSender();

	@Test
	public final void testSuccessResult() {
		SenderResult success = sender.getSuccessResult();
		Assert.assertNotNull(success);
		Assert.assertEquals(success.getStatus(), SenderResult.Status.SUCCESS);
		Assert.assertNull(success.getRemoveDeviceId());
		Assert.assertNull(success.getUpdateDeviceId());
		Assert.assertNull(success.getErrorMsg());
		Assert.assertNull(success.getErrorCause());

	}


	@Test
	public final void testErrorMsgResult() {
		SenderResult error = sender.getErrorResult("error");
		Assert.assertNotNull(error);
		Assert.assertEquals(error.getStatus(), SenderResult.Status.ERROR);
		Assert.assertEquals(error.getErrorMsg(), "error");
		Assert.assertNull(error.getRemoveDeviceId());
		Assert.assertNull(error.getUpdateDeviceId());
		Assert.assertNull(error.getErrorCause());

	}


	@Test
	public final void testErrorCauseResult() {
		Exception exception = new RuntimeException("error");
		SenderResult error = sender.getErrorResult(exception);
		Assert.assertNotNull(error);
		Assert.assertEquals(error.getStatus(), SenderResult.Status.ERROR);
		Assert.assertEquals(error.getErrorCause(), exception);
		Assert.assertNull(error.getRemoveDeviceId());
		Assert.assertNull(error.getUpdateDeviceId());
		Assert.assertNull(error.getErrorMsg());
	}


	@Test
	public final void testClientResults() {
		String oldDeviceId = "idOld";
		String newDeviceId = "idNew";

		SenderResult result = sender.getRemoveClientResult(oldDeviceId);
		Assert.assertNotNull(result);
		Assert.assertEquals(SenderResult.Status.REMOVE_CLIENT, result.getStatus());
		Assert.assertEquals(oldDeviceId, result.getRemoveDeviceId());
		Assert.assertNull(result.getUpdateDeviceId());
		Assert.assertNull(result.getErrorCause());
		Assert.assertNull(result.getErrorMsg());

		result = sender.getUpdateClientResult(oldDeviceId, newDeviceId);
		Assert.assertNotNull(result);
		Assert.assertEquals(SenderResult.Status.UPDATE_CLIENT, result.getStatus());
		Assert.assertNull(result.getRemoveDeviceId());
		Assert.assertEquals(oldDeviceId, result.getUpdateDeviceId().first);
		Assert.assertEquals(newDeviceId, result.getUpdateDeviceId().second);
		Assert.assertNull(result.getErrorCause());
		Assert.assertNull(result.getErrorMsg());

	}


	private static final class DummyNotificationSender extends NotificationSender<Client> {

		@Override
		public SenderResult sendEventUpdate(
				Client client,
				String eventId,
				List<JsonNode> newEvents,
				List<JsonNode> oldEvents) {

			return null;
		}

	}

}
