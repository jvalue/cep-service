package org.jvalue.ceps.notifications.garbage;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jvalue.ceps.api.notifications.GcmClient;
import org.jvalue.ceps.notifications.utils.GcmUtils;

import java.util.Map;

import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;


@RunWith(JMockit.class)
public final class GcmClientGarbageCollectorTest {

	@Mocked private GcmUtils gcmUtils;
	@Mocked private GcmUtils.GcmResult gcmResult;

	@Test
	public void testStatusCollect() {
		testStatus(true);
	}


	@Test
	public void testStatusRetain() {
		testStatus(false);
	}


	@SuppressWarnings("unchecked")
	private void testStatus(final boolean collect) {
		new Expectations() {{
			gcmUtils.sendMsg(anyString, (Map) any);
			result = gcmResult;

			gcmResult.isNotRegistered();
			result = collect;
		}};

		final String deviceId = "someDeviceId";
		GcmClient client = new GcmClient("someClientId", deviceId, "someEplStmt", "someUserId");
		CollectionStatus status = new GcmClientGarbageCollector(gcmUtils).determineStatus(client);

		if (collect) Assert.assertEquals(CollectionStatus.COLLECT, status);
		else Assert.assertEquals(CollectionStatus.RETAIN, status);
		new Verifications() {{
			gcmUtils.sendMsg(deviceId, (Map) any);
		}};
	}

}
