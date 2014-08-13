package org.jvalue.ceps.notifications.garbage;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.jvalue.ceps.notifications.NotificationManager;
import org.jvalue.ceps.notifications.clients.Client;
import org.jvalue.ceps.notifications.clients.ClientVisitor;
import org.jvalue.ceps.notifications.clients.GcmClient;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


@RunWith(PowerMockRunner.class)
@PrepareForTest({NotificationManager.class, Client.class})
public final class GarbageCollectorManagerTest {

	private static final String
		DEVICE_REMOVE_1 = "remove1",
		DEVICE_REMOVE_2 = "remove2",
		DEVICE_RETAIN = "keep";


	@Test
	public void testCollection() throws Exception {
		final long interval = 100;

		Set<Client> dummyClients = new HashSet<Client>();
		dummyClients.add(createClient(DEVICE_REMOVE_1, CollectionStatus.COLLECT));
		dummyClients.add(createClient(DEVICE_REMOVE_2, CollectionStatus.COLLECT));
		dummyClients.add(createClient(DEVICE_RETAIN, CollectionStatus.RETAIN));
		dummyClients.add(createClient(DEVICE_REMOVE_1, CollectionStatus.COLLECT));

		NotificationManager notificatonManager = PowerMockito.mock(NotificationManager.class);
		when(notificatonManager.getAll()).thenReturn(dummyClients);

		GarbageCollectorManager garbageManager = new GarbageCollectorManager(notificatonManager, new Mapper(), 100);

		assertFalse(garbageManager.isRunning());
		garbageManager.startCollection();
		assertTrue(garbageManager.isRunning());

		Thread.sleep((long) (interval * 1.5));

		assertTrue(garbageManager.isRunning());
		garbageManager.stopCollection();
		assertFalse(garbageManager.isRunning());

		verify(notificatonManager, times(1)).unregisterDevice(eq(DEVICE_REMOVE_1));
		verify(notificatonManager, times(1)).unregisterDevice(eq(DEVICE_REMOVE_2));
		verify(notificatonManager, never()).unregisterDevice(eq(DEVICE_RETAIN));

		Thread.sleep((long) (interval * 1.5));
		assertFalse(garbageManager.isRunning());
	}


	@SuppressWarnings("unchecked")
	private Client createClient(String deviceId, CollectionStatus status) {
		Client client = PowerMockito.mock(Client.class);
		when(client.getDeviceId()).thenReturn(deviceId);
		when(client.getClientId()).thenReturn(UUID.randomUUID().toString());
		when(client.getEplStmt()).thenReturn(UUID.randomUUID().toString());
		when(client.accept(any(ClientVisitor.class), any())).thenReturn(status);
		return client;
	}


	private static final class Mapper implements ClientVisitor<Void, CollectionStatus> {

		@Override
		public CollectionStatus visit(GcmClient client, Void param) {
			throw new UnsupportedOperationException("stub");
		}
	}
}

