package org.jvalue.ceps.notifications;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;

import org.jvalue.ceps.api.notifications.Client;
import org.jvalue.ceps.api.notifications.GcmClient;
import org.jvalue.ceps.api.notifications.HttpClient;
import org.jvalue.ceps.db.ClientRepository;
import org.jvalue.ceps.esper.EsperManager;
import org.jvalue.ceps.esper.EventUpdateListener;
import org.jvalue.ceps.event.EventManager;
import org.jvalue.ceps.notifications.sender.NotificationSender;
import org.jvalue.ceps.notifications.sender.SenderResult;
import org.jvalue.ceps.utils.Assert;
import org.jvalue.ceps.utils.BiMap;
import org.jvalue.ceps.utils.Log;
import org.jvalue.commons.auth.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.dropwizard.lifecycle.Managed;


public final class NotificationManager implements EventUpdateListener, Managed {

	private final ClientRepository clientRepository;
	private final Map<Class<?>, NotificationSender<?>> sender;
	private final EsperManager esperManager;
	private final EventManager eventManager;
	private final BiMap<String, String> clientToRegistrationIdMap = new BiMap<>(); // kept in memory es Esper rules are in memory too

	@Inject
	NotificationManager(
			EsperManager esperManager,
			EventManager eventManager,
			NotificationSender<GcmClient> gcmSender,
			NotificationSender<HttpClient> httpSender,
			ClientRepository clientRepository) {

		this.esperManager = esperManager;
		this.eventManager = eventManager;
		this.sender = new HashMap<>();
		this.sender.put(GcmClient.class, gcmSender);
		this.sender.put(HttpClient.class, httpSender);
		this.clientRepository = clientRepository;
	}


	public synchronized void register(Client client) {
		Assert.assertNotNull(client);
		Assert.assertTrue(sender.containsKey(client.getClass()), "unknown client type");

		register(client, true);
	}


	private void register(Client client, boolean addToDb) {
		String registrationId = esperManager.register(client.getEplStmt(), this);
		clientToRegistrationIdMap.put(client.getId(), registrationId);
		if (addToDb) clientRepository.add(client);
	}


	public synchronized boolean unregister(String clientId) {
		Assert.assertNotNull(clientId);
		if (!clientToRegistrationIdMap.containsFirst(clientId)) return false;

		System.out.println("regId = " + clientToRegistrationIdMap.getSecond(clientId));
		esperManager.unregister(clientToRegistrationIdMap.getSecond(clientId));
		clientToRegistrationIdMap.removeFirst(clientId);

		clientRepository.remove(clientRepository.findById(clientId));
		return true;
	}


	public synchronized void unregisterDevice(String deviceId) {
		Assert.assertNotNull(deviceId);

		for (Client client : clientRepository.findByDeviceId(deviceId)) {
			unregister(client.getId());
		}
	}


	public synchronized boolean isRegistered(String clientId) {
		Assert.assertNotNull(clientId);
		return clientToRegistrationIdMap.containsFirst(clientId);
	}


	public synchronized List<Client> getAll() {
		return clientRepository.getAll();
	}


	public synchronized List<Client> getAll(User user) {
		return clientRepository.findByUserId(user.getId());
	}


	public synchronized Client get(String clientId) {
		return clientRepository.findById(clientId);
	}


	@Override
	@SuppressWarnings({"unchecked", "rawtypes"})
	public synchronized void onNewEvents(String registrationId, List<JsonNode> newEvents, List<JsonNode> oldEvents) {
		String eventId = eventManager.onNewEvents(newEvents, oldEvents);
		String clientId = clientToRegistrationIdMap.getFirst(registrationId);
		Client client = clientRepository.findById(clientId);

		Log.info("Sending event to client " + client.getId() + " (deviceId: "  + client.getDeviceId() + ")");
		NotificationSender s = sender.get(client.getClass());
		SenderResult result = s.sendEventUpdate(client, eventId, newEvents, oldEvents);

		switch (result.getStatus()) {
			case SUCCESS:
				break;

			case ERROR:
				Log.error("Failed to send notification to client " + client.getId());
				if (result.getErrorCause() != null) Log.error("cause", result.getErrorCause() );
				else Log.error(result.getErrorMsg());
				break;

			case REMOVE_CLIENT:
				Log.info("Removing client with deviceId " + result.getRemoveDeviceId());
				for (Client removeClient : clientRepository.findByDeviceId(result.getRemoveDeviceId())) {
					unregister(removeClient.getId());
				}
				break;

			case UPDATE_CLIENT:
				Log.info("Updating client " + client.getId());

				DeviceIdUpdater updater = new DeviceIdUpdater();
				String oldDeviceId = result.getUpdateDeviceId().first;
				String newDeviceId = result.getUpdateDeviceId().second;

				for (Client updateClient : clientRepository.findByDeviceId(oldDeviceId)) {
					Client newClient = updateClient.accept(updater, newDeviceId);
					clientRepository.update(newClient);
				}
				break;
		}
	}


	@Override
	public void start() {
		for (Client client : clientRepository.getAll()) {
			register(client, false);
		}
	}


	@Override
	public void stop() {
		// nothing to do
	}

}
