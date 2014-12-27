package org.jvalue.ceps.notifications;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;

import org.jvalue.ceps.db.ClientRepository;
import org.jvalue.ceps.esper.EsperManager;
import org.jvalue.ceps.esper.JsonUpdateListener;
import org.jvalue.ceps.event.EventManager;
import org.jvalue.ceps.notifications.clients.Client;
import org.jvalue.ceps.notifications.clients.DeviceIdUpdater;
import org.jvalue.ceps.notifications.clients.GcmClient;
import org.jvalue.ceps.notifications.sender.NotificationSender;
import org.jvalue.ceps.notifications.sender.SenderResult;
import org.jvalue.ceps.utils.Assert;
import org.jvalue.ceps.utils.BiMap;
import org.jvalue.ceps.utils.Log;
import org.jvalue.ceps.utils.Restoreable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public final class NotificationManager implements JsonUpdateListener, Restoreable {

	private static NotificationManager instance;

	public static synchronized NotificationManager getInstance() {
		/*
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
		*/
		return null;
	}


	private final ClientRepository clientRepository;
	private final Map<Class<?>, NotificationSender<?>> sender;
	private final EsperManager esperManager;
	private final EventManager eventManager;
	private final BiMap<String, String> clientToStmtMap = new BiMap<>();

	@Inject
	NotificationManager(
			EsperManager esperManager,
			EventManager eventManager,
			NotificationSender<GcmClient> gcmSender,
			ClientRepository clientRepository) {

		this.esperManager = esperManager;
		this.eventManager = eventManager;
		this.sender = new HashMap<>();
		this.sender.put(GcmClient.class, gcmSender);
		this.clientRepository = clientRepository;
	}


	public synchronized void register(Client client) {
		Assert.assertNotNull(client);
		Assert.assertTrue(sender.containsKey(client.getClass()), "unknown client type");

		register(client, true);
	}


	private void register(Client client, boolean addToDb) {
		String stmtId = esperManager.register(client.getEplStmt(), this);
		clientToStmtMap.put(client.getClientId(), stmtId);
		if (addToDb) clientRepository.add(client);
	}


	public synchronized boolean unregister(String clientId) {
		Assert.assertNotNull(clientId);
		if (!clientToStmtMap.containsFirst(clientId)) return false;

		esperManager.unregister(clientToStmtMap.getSecond(clientId));
		clientToStmtMap.removeFirst(clientId);

		clientRepository.remove(clientRepository.findByClientId(clientId));
		return true;
	}


	public synchronized void unregisterDevice(String deviceId) {
		Assert.assertNotNull(deviceId);

		for (Client client : clientRepository.getAll()) {
			if (client.getDeviceId().equals(deviceId)) unregister(client.getClientId());
		}
	}


	public synchronized boolean isRegistered(String clientId) {
		Assert.assertNotNull(clientId);
		return clientToStmtMap.containsFirst(clientId);
	}


	public synchronized Set<Client> getAll() {
		return new HashSet<Client>(clientRepository.getAll());
	}


	@Override
	@SuppressWarnings({"unchecked", "rawtypes"})
	public synchronized void onNewEvents(String eplStmtId, List<JsonNode> newEvents, List<JsonNode> oldEvents) {
		String eventId = eventManager.onNewEvents(newEvents, oldEvents);
		String clientId = clientToStmtMap.getFirst(eplStmtId);
		Client client = clientRepository.findByClientId(clientId);

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
				Log.info("Removing client with deviceId " + result.getRemoveDeviceId());
				for (Client removeClient : clientRepository.getAll()) {
					if (removeClient.getDeviceId().equals(result.getRemoveDeviceId())) {
						unregister(removeClient.getClientId());
					}
				}
				break;

			case UPDATE_CLIENT:
				Log.info("Updating client " + client.getClientId());

				DeviceIdUpdater updater = new DeviceIdUpdater();
				String oldDeviceId = result.getUpdateDeviceId().first;
				String newDeviceId = result.getUpdateDeviceId().second;

				for (Client updateClient : clientRepository.getAll()) {
					if (updateClient.getDeviceId().equals(oldDeviceId)) {
						Client newClient = updateClient.accept(updater, newDeviceId);
						unregister(updateClient.getClientId());
						register(newClient);
					}
				}
				break;
		}
	}


	@Override
	public synchronized void restoreState() {
		Log.info("Restoring state for " + NotificationManager.class.getSimpleName());
		for (Client client : clientRepository.getAll()) {
			register(client, false);
		}
	}

}
