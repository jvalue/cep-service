package org.jvalue.ceps.notifications.garbage;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.jvalue.ceps.api.notifications.Client;
import org.jvalue.ceps.api.notifications.ClientVisitor;
import org.jvalue.ceps.api.notifications.GcmClient;
import org.jvalue.ceps.notifications.NotificationManager;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;


@RunWith(JMockit.class)
public final class ClientGarbageCollectorManagerTest {

	@Mocked private NotificationManager notificationManager;
	@Mocked private ClientVisitor<Void, CollectionStatus> mapper;

	private static final String
		DEVICE_REMOVE_1 = "remove1",
		DEVICE_REMOVE_2 = "remove2",
		DEVICE_RETAIN = "keep";


	@Test
	public void testCollection() throws Exception {
		new Expectations() {{
			List<Client> clients = new LinkedList<>();
			GcmClient client1 = new GcmClient("clientId1", DEVICE_REMOVE_1, "adapterId", new HashMap<String, Object>(), "userId");
			GcmClient client2 = new GcmClient("clientId2", DEVICE_REMOVE_2, "adapterId", new HashMap<String, Object>(), "userId");
			GcmClient client3 = new GcmClient("clientId3", DEVICE_RETAIN, "adapterId", new HashMap<String, Object>(), "userId");
			clients.addAll(Arrays.asList(client1, client2, client3));

			notificationManager.getAll();
			result = clients;

			mapper.visit(client1, null); result = CollectionStatus.COLLECT;
			mapper.visit(client2, null); result = CollectionStatus.COLLECT;
			mapper.visit(client3, null); result = CollectionStatus.RETAIN;
		}};

		ClientGarbageCollectorManager collectorManager = new ClientGarbageCollectorManager(notificationManager, mapper, 500);
		collectorManager.start();
		Thread.sleep(200);
		collectorManager.stop();

		new Verifications() {{
			notificationManager.unregisterDevice(DEVICE_REMOVE_1); times = 1;
			notificationManager.unregisterDevice(DEVICE_REMOVE_2); times = 1;
			notificationManager.unregisterDevice(DEVICE_RETAIN); times = 0;
		}};
	}

}

