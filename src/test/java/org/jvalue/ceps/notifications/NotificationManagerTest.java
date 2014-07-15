package org.jvalue.ceps.notifications;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.jvalue.ceps.esper.DummyEsperManager;
import org.jvalue.ceps.esper.EsperManager;
import org.jvalue.ceps.event.DummyEventManager;
import org.jvalue.ceps.event.EventManager;
import org.jvalue.ceps.notifications.clients.DummyClient;
import org.jvalue.ceps.notifications.sender.NotificationSender;
import org.jvalue.ceps.notifications.sender.SenderResult;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;


public final class NotificationManagerTest {

	private static final ObjectMapper mapper = new ObjectMapper();

	private static final String dataType = "pegelonline";

	private static DummyClient client;
	private static DummyClient newClient;

	private static EsperManager esperManager;
	private static NotificationManager notificationManager;
	private static DummyNotificationSender sender;

	private  static int sendEventUpdateCount = 0;

	@BeforeClass
	public static void setup() throws Exception {

		client = new DummyClient("dummy", "select * from " 
				+ dataType + ".win:length(1) where longname = 'EITZE'");
		newClient = new DummyClient("dummy2", "select longname from " 
				+ dataType + ".win:length(1) where longname = 'EITZE'");

		EventManager eventManager = DummyEventManager.createInstance();
		esperManager = DummyEsperManager.createInstance("NotificationManagerTest");
		sender = new DummyNotificationSender();

		Map<Class<?>, NotificationSender<?>> senderMap = new HashMap<>();
		senderMap.put(DummyClient.class, sender);

		notificationManager = DummyNotificationManager.createInstance(
				esperManager,
				eventManager,
				senderMap);

		esperManager.onNewDataType(dataType, getResource("/schema-pegelonline.json"));
	}


	@Before
	public void registerClient() {
		assertFalse(notificationManager.isRegistered(client.getClientId()));
		notificationManager.register(client);
		assertTrue(notificationManager.isRegistered(client.getClientId()));
	}


	@After
	public void unregisterClients() {
		assertTrue(notificationManager.isRegistered(client.getClientId()));
		notificationManager.unregister(client.getClientId());
		assertFalse(notificationManager.isRegistered(client.getClientId()));
	}


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
		testResult(SenderResult.Status.UPDATE_CLIENT);
		assertTrue(notificationManager.isRegistered(newClient.getClientId()));
		assertFalse(notificationManager.isRegistered(client.getClientId()));
		notificationManager.unregister(newClient.getClientId());
		notificationManager.register(client);
	}


	private void testResult(SenderResult.Status status) throws Exception {
		sender.setStatus(status);
		esperManager.onNewData(dataType, new ArrayNode(JsonNodeFactory.instance)
				.add(getResource("/data-pegelonline-eitze.json")));
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
					return getUpdateClientResult(client, newClient);
				case REMOVE_CLIENT:
					return getRemoveClientResult(client);
				case ERROR:
					return getErrorResult("error");
			}
			return null;
		}

	}

}
