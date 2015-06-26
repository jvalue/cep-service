package org.jvalue.ceps.notifications;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jvalue.ceps.adapter.EplAdapterManager;
import org.jvalue.ceps.api.notifications.Client;
import org.jvalue.ceps.api.notifications.GcmClient;
import org.jvalue.ceps.api.notifications.HttpClient;
import org.jvalue.ceps.db.ClientRepository;
import org.jvalue.ceps.esper.EsperManager;
import org.jvalue.ceps.esper.EventUpdateListener;
import org.jvalue.ceps.notifications.sender.NotificationSender;
import org.jvalue.ceps.notifications.sender.SenderResult;
import org.jvalue.ceps.utils.Pair;

import java.util.Arrays;
import java.util.HashMap;

import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;


@RunWith(JMockit.class)
public final class NotificationManagerTest {

	private static final String
			CLIENT_ID = "someClientId",
			DEVICE_ID = "someDeviceId",
			USER_ID = "someUserId",
			EPL_ADAPTER_ID = "someAdapterId";

	@Mocked private EsperManager esperManager;
	@Mocked private EplAdapterManager eplAdapterManager;
	@Mocked private NotificationSender<GcmClient> gcmSender;
	@Mocked private NotificationSender<HttpClient> httpSender;
	@Mocked private ClientRepository clientRepository;
	@Mocked private SenderResult senderResult;

	private NotificationManager notificationManager;

	private final Client client = new GcmClient(CLIENT_ID, DEVICE_ID, EPL_ADAPTER_ID, new HashMap<String, Object>(), USER_ID);

	@Before
	public void setupNotificationManager() {
		this.notificationManager = new NotificationManager(esperManager, eplAdapterManager, gcmSender, httpSender, clientRepository);
	}


	@Test
	public void testRegister() {
		notificationManager.register(client);

		new Verifications() {{
			esperManager.register(anyString, notificationManager);
			clientRepository.add(client);
		}};
	}


	@Test
	public void testUnregister() {
		final String registrationId = "someRegId";
		new Expectations() {{
			esperManager.register(anyString, (EventUpdateListener) any);
			result = registrationId;

			clientRepository.findById(CLIENT_ID);
			result = client;
		}};

		notificationManager.register(client);
		notificationManager.unregister(CLIENT_ID);

		new Verifications() {{
			esperManager.unregister(registrationId);
			clientRepository.remove(client);
		}};
	}


	@Test
	public void testUnregisterDevice() {
		new Expectations() {{
			clientRepository.findByDeviceId(DEVICE_ID);
			result = Arrays.asList(client);

			clientRepository.findById(CLIENT_ID);
			result = client;

			esperManager.register(anyString, (EventUpdateListener) any);
			result = "someRegId";
		}};

		notificationManager.register(client);
		notificationManager.unregisterDevice(DEVICE_ID);

		new Verifications() {{
			clientRepository.remove(client);
		}};
	}


	@Test
	public void testGetAll() {
		notificationManager.getAll();

		new Verifications() {{
			clientRepository.getAll();
		}};
	}


	@Test
	public void testStart() {
		new Expectations() {{
			clientRepository.getAll();
			result = Arrays.asList(client);
		}};

		notificationManager.start();

		new Verifications() {{
			esperManager.register(anyString, notificationManager);
			clientRepository.add(client); times = 0;
		}};
	}


	@Test
	public void testIsRegistered() {
		Assert.assertFalse(notificationManager.isRegistered(CLIENT_ID));
		notificationManager.register(client);
		Assert.assertTrue(notificationManager.isRegistered(CLIENT_ID));
		notificationManager.unregister(CLIENT_ID);
		Assert.assertFalse(notificationManager.isRegistered(CLIENT_ID));
	}




	@Test
	public void testSuccess() {
		new Expectations() {{
			senderResult.getStatus(); result = SenderResult.Status.SUCCESS;
		}};
		testResult(senderResult);
	}


	@Test
	public void testError() {
		new Expectations() {{
			senderResult.getStatus(); result = SenderResult.Status.ERROR;
		}};
		testResult(senderResult);
	}


	@Test
	public void testRemoveClient() {
		new Expectations() {{
			senderResult.getStatus();
			result = SenderResult.Status.REMOVE_CLIENT;

			senderResult.getRemoveDeviceId();
			result = DEVICE_ID;
		}};

		testResult(senderResult);

		new Verifications() {{
			notificationManager.unregisterDevice(DEVICE_ID);
		}};
	}


	@Test
	public void testUpdateClient() {
		final String newDeviceId = "someNewDeviceId";
		new Expectations() {{
			senderResult.getStatus();
			result = SenderResult.Status.UPDATE_CLIENT;

			senderResult.getUpdateDeviceId();
			result = new Pair<>(DEVICE_ID, newDeviceId);
		}};

		testResult(senderResult);

		new Verifications() {{
			Client newClient;
			clientRepository.update(newClient = withCapture());

			Assert.assertEquals(newDeviceId, newClient.getDeviceId());
			Assert.assertEquals(CLIENT_ID, newClient.getId());
			Assert.assertEquals(EPL_ADAPTER_ID, newClient.getEplAdapterId());
		}};
	}


	@SuppressWarnings("unchecked")
	private void testResult(final SenderResult senderResult) {
		final String registrationId = "someRegId";
		new Expectations() {{
			esperManager.register(anyString, (EventUpdateListener) any);
			result = registrationId;

			clientRepository.findById(CLIENT_ID);
			result = client;
			clientRepository.findByDeviceId(DEVICE_ID);
			result = Arrays.asList(client); minTimes = 0;

			gcmSender.sendEventUpdate((GcmClient) any);
			result = senderResult;
		}};

		notificationManager.register(client);
		notificationManager.onNewEvents(registrationId);

		new Verifications() {{
			gcmSender.sendEventUpdate((GcmClient) client);
			times = 1;
		}};
	}

}
