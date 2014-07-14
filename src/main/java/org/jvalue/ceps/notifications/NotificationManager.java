package org.jvalue.ceps.notifications;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jvalue.ceps.db.DbAccessorFactory;
import org.jvalue.ceps.db.JsonObjectDb;
import org.jvalue.ceps.esper.EsperManager;
import org.jvalue.ceps.esper.JsonUpdateListener;
import org.jvalue.ceps.notifications.clients.Client;
import org.jvalue.ceps.notifications.clients.GcmClient;
import org.jvalue.ceps.notifications.sender.NotificationSender;
import org.jvalue.ceps.notifications.sender.SenderFactory;
import org.jvalue.ceps.utils.Assert;

import com.fasterxml.jackson.databind.JsonNode;


public final class NotificationManager {

	private static final String DB_NAME = "cepsClients";

	private static NotificationManager instance;

	public static NotificationManager getInstance() {
		if (instance == null) {
			EsperManager manager = EsperManager.getInstance();

			Map<Class<?>, NotificationSender<?>> sender = new HashMap<>();
			sender.put(GcmClient.class, SenderFactory.getGcmSender());

			JsonObjectDb<Client> clientDb = new JsonObjectDb<Client>(
					DbAccessorFactory.getCouchDbAccessor(DB_NAME),
					Client.class);

			instance = new NotificationManager(manager, sender, clientDb);
		}
		return instance;
	}


	private final JsonObjectDb<Client> clientDb;
	private final Map<Class<?>, NotificationSender<?>> sender;
	private final EsperManager esperManager;
	private final Map<String, String> clientToStmtMap = new HashMap<String, String>();

	private NotificationManager(
			EsperManager esperManager,
			Map<Class<?>, NotificationSender<?>> sender, 
			JsonObjectDb<Client> clientDb) {

		Assert.assertNotNull(esperManager, sender, clientDb);
		this.esperManager = esperManager;
		this.sender = sender;
		this.clientDb = clientDb;
	}


	@SuppressWarnings("unchecked")
	public <C extends Client> void register(C client) {
		Assert.assertNotNull(client);
		Assert.assertTrue(sender.containsKey(client.getClass()), "unknown client type");

		NotificationSender<C> s = (NotificationSender<C>) sender.get(client.getClass());
		EsperUpdateListener<?> listener = new EsperUpdateListener<C>(client, s);

		String stmtId = esperManager.register(client.getEplStmt(), listener);

		clientToStmtMap.put(client.getClientId(), stmtId);
		clientDb.add(client);
	}


	public void unregister(Client client) {
		Assert.assertNotNull(client);
		Assert.assertTrue(clientToStmtMap.containsKey(client.getClientId()), "not registered");

		String clientId = client.getClientId();
		esperManager.unregister(clientToStmtMap.get(clientId));
		clientToStmtMap.remove(clientId);
		clientDb.remove(client);
	}


	private static class EsperUpdateListener<C extends Client> implements JsonUpdateListener {

		private final C client;
		private final NotificationSender<C> sender;

		public EsperUpdateListener(C client, NotificationSender<C> sender) {
			Assert.assertNotNull(client, sender);
			this.client = client;
			this.sender = sender;
		}

		public void onNewEvents(List<JsonNode> newEvents, List<JsonNode> oldEvents) {
			sender.sendEventUpdate(client, newEvents, oldEvents);
		}

	}

}
