package org.jvalue.ceps.notifications;

import java.util.Map;

import org.jvalue.ceps.db.DbAccessorFactory;
import org.jvalue.ceps.db.JsonObjectDb;
import org.jvalue.ceps.utils.Assert;


public final class NotificationManager {

	private static final String DB_NAME = "cepsClients";

	private static NotificationManager instance;

	public static NotificationManager getInstance() {
		if (instance == null) {
			instance = new NotificationManager(
					new JsonObjectDb<Client>(
						DbAccessorFactory.getCouchDbAccessor(DB_NAME),
						Client.class));
		}
		return instance;
	}


	private final JsonObjectDb<Client> clientDb;
	private final Map<Class<?>, NotificationDefinition<?>> defintions = null;

	private NotificationManager(JsonObjectDb<Client> clientDb) {
		Assert.assertNotNull(clientDb);
		this.clientDb = clientDb;
	}


	@SuppressWarnings({"rawtypes", "unchecked"})
	public void register(Client client) {
		Assert.assertNotNull(client);

		NotificationSender sender = defintions.get(client.getClass()).getSender();
		ClientEplMapper.register(client, sender);
		clientDb.add(client);
	}


	public void unregister(Client client) {
		Assert.assertNotNull(client);
		unregister(client.getClientId());
	}


	public void unregister(String clientId) {
		Assert.assertNotNull(clientId);
		// ClientEplMapper.unregister(clientId);
	}


}
