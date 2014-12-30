package org.jvalue.ceps.notifications;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.jvalue.ceps.esper.EsperManager;


public final class NotificationManagerTest {

	private static final ObjectMapper mapper = new ObjectMapper();

	private static final String dataType = "pegelonline";

	private static String newDeviceId;

	private static EsperManager esperManager;
	private static NotificationManager notificationManager;

	private  static int sendEventUpdateCount = 0;

	@BeforeClass
	public static void setup() throws Exception {

		/*
		client = new DummyClient("dummy", "dumm2", "select * from " 
				+ dataType + ".win:length(1) where longname = 'EITZE'");
		newDeviceId = "dummy3";

		// TODO
		EventManager eventManager = null;
		// esperManager = DummyEsperManager.createInstance("NotificationManagerTest");
		sender = new DummyNotificationSender();

		Map<Class<?>, NotificationSender<?>> senderMap = new HashMap<>();
		senderMap.put(DummyClient.class, sender);

		notificationManager = DummyNotificationManager.createInstance(
				esperManager,
				eventManager,
				senderMap);

		esperManager.onSourceAdded(dataType, getResource("/schema-pegelonline.json"));
		*/
	}


	@Before
	public void registerClient() {
		/*
		assertFalse(notificationManager.isRegistered(client.getClientId()));
		notificationManager.register(client);
		assertTrue(notificationManager.isRegistered(client.getClientId()));
		*/
	}


	@After
	public void unregisterClients() {
		/*
		assertTrue(notificationManager.isRegistered(client.getClientId()));
		notificationManager.unregister(client.getClientId());
		assertFalse(notificationManager.isRegistered(client.getClientId()));
		*/
	}

	/*

	@After
	public void resetSendEventUpdateCount() {
		sendEventUpdateCount = 0;
	}


	@Test
	public void testSuccess() throws Exception {
		testResult(SenderResult.Status.SUCCESS);
	}


	@Test
	public void testError() throws Exception {
		testResult(SenderResult.Status.ERROR);
	}


	@Test
	public void testRemoveClient() throws Exception {
		testResult(SenderResult.Status.REMOVE_CLIENT);
		assertFalse(notificationManager.isRegistered(client.getClientId()));
		notificationManager.register(client);
	}


	@Test
	public void testUpdateClient() throws Exception {
		assertTrue(notificationManager.isRegistered(client.getClientId()));
		assertEquals(1, notificationManager.getAll().size());
		assertEquals(client.getDeviceId(), notificationManager.getAll().iterator().next().getDeviceId());

		testResult(SenderResult.Status.UPDATE_CLIENT);

		assertTrue(notificationManager.isRegistered(client.getClientId()));
		assertEquals(1, notificationManager.getAll().size());
		assertEquals(newDeviceId, notificationManager.getAll().iterator().next().getDeviceId());

		notificationManager.unregister(client.getClientId());
		notificationManager.register(client);
	}


	@Test
	public void testUnregisterDevice() {
		notificationManager.unregisterDevice(client.getDeviceId());
		assertFalse(notificationManager.isRegistered(client.getClientId()));
		registerClient();
	}



	private void testResult(SenderResult.Status status) throws Exception {
		sender.setStatus(status);
		// esperManager.onNewSourceData(dataType, getResource("/data-pegelonline1.json"));
		// TODO
		assertEquals(1, sendEventUpdateCount);
	}


	private static JsonNode getResource(String name) throws Exception {
		URL url = NotificationManagerTest.class.getResource(name);
		return mapper.readTree(new File(url.toURI()));
	}


	private static final class DummyNotificationSender extends NotificationSender<DummyClient> {

		private SenderResult.Status status;

		public void setStatus(SenderResult.Status status) {
			this.status = status;
		}

		@Override
		public SenderResult sendEventUpdate(
				DummyClient client, 
				String eventId, 
				List<JsonNode> newEvents, 
				List<JsonNode> oldEvents) {

			assertEquals(NotificationManagerTest.client, client);
			assertNotNull(newEvents);
			assertNotNull(oldEvents);

			sendEventUpdateCount++;

			switch(status) {
				case SUCCESS:
					return getSuccessResult();
				case UPDATE_CLIENT:
					System.out.println("called test update");
					return getUpdateClientResult(client.getDeviceId(), newDeviceId);
				case REMOVE_CLIENT:
					return getRemoveClientResult(client.getDeviceId());
				case ERROR:
					return getErrorResult("error");
			}
			return null;
		}

	}
	*/

}
