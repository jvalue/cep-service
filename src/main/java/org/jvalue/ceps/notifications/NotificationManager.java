package org.jvalue.ceps.notifications;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jvalue.ceps.db.DbAccessorFactory;
import org.jvalue.ceps.db.JsonObjectDb;
import org.jvalue.ceps.esper.EsperManager;
import org.jvalue.ceps.esper.JsonUpdateListener;
import org.jvalue.ceps.event.EventManager;
import org.jvalue.ceps.notifications.clients.Client;
import org.jvalue.ceps.notifications.clients.GcmClient;
import org.jvalue.ceps.notifications.sender.NotificationSender;
import org.jvalue.ceps.notifications.sender.SenderFactory;
import org.jvalue.ceps.notifications.sender.SenderResult;
import org.jvalue.ceps.utils.Assert;
import org.jvalue.ceps.utils.BiMap;
import org.jvalue.ceps.utils.Log;

import com.fasterxml.jackson.databind.JsonNode;


public final class NotificationManager implements JsonUpdateListener {

	private static final String DB_NAME = "cepsClients";

	private static NotificationManager instance;

	public static NotificationManager getInstance() {
		if (instance == null) {
			EsperManager esperManager = EsperManager.getInstance();
			EventManager eventManager = EventManager.getInstance();

			Map<Class<?>, NotificationSender<?>> sender = new HashMap<>();
			sender.put(GcmClient.class, SenderFactory.getGcmSender());

			JsonObjectDb<Client> clientDb = new JsonObjectDb<Client>(
					DbAccessorFactory.getCouchDbAccessor(DB_NAME),
					Client.class);

			instance = new NotificationManager(esperManager, eventManager, sender, clientDb);
		}
		return instance;
	}


	private final JsonObjectDb<Client> clientDb;
	private final Map<Class<?>, NotificationSender<?>> sender;
	private final EsperManager esperManager;
	private final EventManager eventManager;
	private final BiMap<String, String> clientToStmtMap = new BiMap<String, String>();

	private NotificationManager(
			EsperManager esperManager,
			EventManager eventManager,
			Map<Class<?>, NotificationSender<?>> sender, 
			JsonObjectDb<Client> clientDb) {

		Assert.assertNotNull(esperManager, eventManager, sender, clientDb);
		this.esperManager = esperManager;
		this.eventManager = eventManager;
		this.sender = sender;
		this.clientDb = clientDb;
	}


	public void register(Client client) {
		Assert.assertNotNull(client);
		Assert.assertTrue(sender.containsKey(client.getClass()), "unknown client type");

		String stmtId = esperManager.register(client.getEplStmt(), this);
		clientToStmtMap.put(client.getClientId(), stmtId);
		clientDb.add(client);
	}


	public void unregister(String clientId) {
		Assert.assertNotNull(clientId);
		Assert.assertTrue(clientToStmtMap.containsFirst(clientId), "not registered");

		esperManager.unregister(clientToStmtMap.getSecond(clientId));
		clientToStmtMap.removeFirst(clientId);

		Client client = getClientForId(clientId);
		if (client != null) clientDb.remove(client);
	}


	@Override
	@SuppressWarnings({"unchecked", "rawtypes"})
	public void onNewEvents(String eplStmtId, List<JsonNode> newEvents, List<JsonNode> oldEvents) {
		String eventId = eventManager.onNewEvents(newEvents, oldEvents);
		String clientId = clientToStmtMap.getFirst(eplStmtId);
		Client client = getClientForId(clientId);

		NotificationSender s = sender.get(client.getClass());
		SenderResult result = s.sendEventUpdate(client, eventId, newEvents, oldEvents);

		switch (result.getStatus()) {
			case SUCCESS:
				break;

			case ERROR:
				Log.error("Failed to send notification to client " + client.getClientId());
				if (result.getErrorCause() != null) Log.error("cause", result.getErrorCause() );
				else Log.error(result.getErrorMsg());
				break;

			case REMOVE_CLIENT:
				Log.info("Removing client " + client.getClientId());
				unregister(result.getOldClient().getClientId());
				break;

			case UPDATE_CLIENT:
				Log.info("Updating client " + client.getClientId());
				unregister(result.getOldClient().getClientId());
				register(result.getNewClient());
				break;
		}
	}


	private Client getClientForId(String clientId) {
		for (Client client : clientDb.getAll()) {
			if (client.getClientId().equals(clientId))
				return client;
		}
		return null;
	}

}
