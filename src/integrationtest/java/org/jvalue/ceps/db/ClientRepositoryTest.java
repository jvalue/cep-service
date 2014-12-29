package org.jvalue.ceps.db;


import org.ektorp.CouchDbInstance;
import org.junit.Assert;
import org.junit.Test;
import org.jvalue.ceps.notifications.clients.Client;
import org.jvalue.ceps.notifications.clients.GcmClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ClientRepositoryTest extends AbstractRepositoryTest {

	private static final String
			CLIENT_ID_1 = "clientId1",
			CLIENT_ID_2 = "clientId2";



	private ClientRepository clientRepository;

	public ClientRepositoryTest() {
		super(ClientRepositoryTest.class.getSimpleName());
	}


	@Override
	protected void createDatabase(CouchDbInstance couchDbInstance, String databaseName) {
		this.clientRepository = new ClientRepository(couchDbInstance.createConnector(databaseName, true));
	}


	@Test
	public void testGetAll() {
		Map<String, Client> clientsMap = setupClientData();
		List<Client> receivedClients = clientRepository.getAll();

		Assert.assertEquals(clientsMap.size(), receivedClients.size());
		for (Client client : receivedClients) {
			Assert.assertNotNull(clientsMap.remove(client.getClientId()));
		}
	}


	@Test
	public void testFindByClientId() {
		Map<String, Client> clients = setupClientData();
		for (Map.Entry<String, Client> entry : clients.entrySet()) {
			Client client = clientRepository.findByClientId(entry.getKey());
			Assert.assertEquals(entry.getValue(), client);
		}
	}


	private Map<String, Client> setupClientData() {
		Map<String, Client> clients = new HashMap<>();
		clients.put(CLIENT_ID_1, new GcmClient(CLIENT_ID_1, "someGcmId", "someEplStmt"));
		clients.put(CLIENT_ID_2, new GcmClient(CLIENT_ID_2, "someGcmId", "someEplStmt"));

		for (Client client : clients.values()) clientRepository.add(client);
		return clients;
	}

}
