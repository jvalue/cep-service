package org.jvalue.ceps.db;


import org.ektorp.CouchDbInstance;
import org.junit.Assert;
import org.junit.Test;
import org.jvalue.ceps.notifications.clients.Client;
import org.jvalue.ceps.notifications.clients.GcmClient;

import java.util.HashMap;
import java.util.Map;

public final class ClientRepositoryTest extends AbstractRepositoryTest<ClientRepository, Client> {

	private static final String
			CLIENT_ID_1 = "clientId1",
			CLIENT_ID_2 = "clientId2";


	public ClientRepositoryTest() {
		super(ClientRepositoryTest.class.getSimpleName());
	}


	@Test
	public void testFindByClientId() {
		Map<String, Client> clients = doSetupDataItems();
		for (Client client : clients.values()) repository.add(client);

		for (Map.Entry<String, Client> entry : clients.entrySet()) {
			Client client = repository.findByClientId(entry.getKey());
			Assert.assertEquals(entry.getValue(), client);
		}
	}


	@Override
	protected ClientRepository doCreateDatabase(CouchDbInstance couchDbInstance, String databaseName) {
		return new ClientRepository(couchDbInstance.createConnector(databaseName, true));
	}


	@Override
	protected Map<String, Client> doSetupDataItems() {
		Map<String, Client> clients = new HashMap<>();
		clients.put(CLIENT_ID_1, new GcmClient(CLIENT_ID_1, "someGcmId", "someEplStmt"));
		clients.put(CLIENT_ID_2, new GcmClient(CLIENT_ID_2, "someGcmId", "someEplStmt"));
		return clients;
	}


	@Override
	protected String doGetIdForItem(Client client) {
		return client.getClientId();
	}

}
