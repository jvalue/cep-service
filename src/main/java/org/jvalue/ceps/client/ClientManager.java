package org.jvalue.ceps.client;

import java.util.List;
import java.util.UUID;

import org.jvalue.ceps.db.DbAccessorFactory;
import org.jvalue.ceps.db.JsonObjectDb;
import org.jvalue.ceps.esper.EsperManager;
import org.jvalue.ceps.utils.Assert;


public final class ClientManager {

	private static final String DB_NAME = "cepsClients";

	private static ClientManager instance;

	public static ClientManager getInstance() {
		if (instance == null) instance = new ClientManager(
				new JsonObjectDb<Client>(
					DbAccessorFactory.getCouchDbAccessor(DB_NAME),
					Client.class));
		return instance;
	}


	private final JsonObjectDb<Client> clientDb;

	private ClientManager(JsonObjectDb<Client> clientDb) {
		Assert.assertNotNull(clientDb);
		this.clientDb = clientDb;
	}


	public String register(String eplStmt, ClientUpdateListener listener) {
		Assert.assertNotNull(eplStmt, listener);

		String clientId = UUID.randomUUID().toString();
		EsperUpdateListener esperListener = new EsperUpdateListener(clientId, listener);
		String stmtId = EsperManager.getInstance().register(eplStmt, esperListener);

		Client client = new Client(clientId, stmtId);
		clientDb.add(client);

		return clientId;
	}


	public void unregister(String clientId) {
		Assert.assertNotNull(clientId);

		List<Client> clients = clientDb.getAll();
		Client client = null;
		for (Client storedClient : clients) {
			if (storedClient.getClientId().equals(clientId)) {
				client = storedClient;
				break;
			}
		}
		Assert.assertTrue(client != null, "client not registered");

		EsperManager.getInstance().unregister(client.getStmtId());
	}

}
