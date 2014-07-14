package org.jvalue.ceps.notifications;

import java.util.HashMap;
import java.util.Map;

import org.jvalue.ceps.db.DbAccessorFactory;
import org.jvalue.ceps.db.JsonObjectDb;
import org.jvalue.ceps.notifications.clients.Client;
import org.jvalue.ceps.notifications.clients.GcmClient;
import org.jvalue.ceps.notifications.sender.NotificationSender;
import org.jvalue.ceps.notifications.sender.SenderFactory;
import org.jvalue.ceps.utils.Assert;


public final class NotificationManager {

	private static final String DB_NAME = "cepsClients";

	private static NotificationManager instance;

	public static NotificationManager getInstance() {
		if (instance == null) {
			Map<Class<?>, NotificationSender<?>> sender = new HashMap<>();
			sender.put(GcmClient.class, SenderFactory.getGcmSender());
			JsonObjectDb<Client> clientDb = new JsonObjectDb<Client>(
					DbAccessorFactory.getCouchDbAccessor(DB_NAME),
					Client.class);

			instance = new NotificationManager(sender, clientDb);
		}
		return instance;
	}


	private final JsonObjectDb<Client> clientDb;
	private final Map<Class<?>, NotificationSender<?>> sender;

	private NotificationManager(
			Map<Class<?>, NotificationSender<?>> sender, 
			JsonObjectDb<Client> clientDb) {

		Assert.assertNotNull(sender, clientDb);
		this.sender = sender;
		this.clientDb = clientDb;
	}


	@SuppressWarnings({"rawtypes", "unchecked"})
	public void register(Client client) {
		Assert.assertNotNull(client);
		Assert.assertTrue(sender.containsKey(client.getClass()), "unknown client type");

		NotificationSender s = sender.get(client.getClass());
		ClientEplMapper.register(client, s);
		clientDb.add(client);
	}


	public void unregister(Client client) {
		Assert.assertNotNull(client);
		ClientEplMapper.unregister(client.getClientId());
		clientDb.remove(client);
	}

}
