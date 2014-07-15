package org.jvalue.ceps.notifications.sender;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;


public final class SenderFactoryTest {

	@Test
	public void testGet() {
		NotificationSender<?> sender1 = SenderFactory.getGcmSender();
		assertNotNull(sender1);
		assertTrue(sender1 == SenderFactory.getGcmSender());
	}

}
