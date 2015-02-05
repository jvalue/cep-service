package org.jvalue.ceps.notifications.sender;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;


public final class SenderResultTest {

	@Test
	public void testEmptyBuild() {

		SenderResult result = new SenderResult.Builder(SenderResult.Status.UPDATE_CLIENT).build();
		assertEquals(result.getStatus(), SenderResult.Status.UPDATE_CLIENT);
		assertNull(result.getRemoveDeviceId());
		assertNull(result.getUpdateDeviceId());
		assertNull(result.getErrorCause());
		assertNull(result.getErrorMsg());

	}


	@Test
	public void testFullBuild() {

		Throwable throwable  = new RuntimeException("bang");
		String errorMsg = "error";
		String oldId = "oldId";
		String newId = "newId";

		SenderResult result = new SenderResult.Builder(SenderResult.Status.REMOVE_CLIENT)
			.removeDeviceId(oldId)
			.updateDeviceId(oldId, newId)
			.errorCause(throwable)
			.errorMsg(errorMsg)
			.build();

		assertEquals(result.getStatus(), SenderResult.Status.REMOVE_CLIENT);
		assertEquals(result.getRemoveDeviceId(), oldId);
		assertEquals(result.getUpdateDeviceId().first, oldId);
		assertEquals(result.getUpdateDeviceId().second, newId);
		assertEquals(result.getErrorCause(), throwable);
		assertEquals(result.getErrorMsg(), errorMsg);

	}

}
