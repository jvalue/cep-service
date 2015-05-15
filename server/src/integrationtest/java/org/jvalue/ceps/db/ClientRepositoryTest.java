package org.jvalue.ceps.db;


import org.junit.Assert;
import org.junit.Test;
import org.jvalue.ceps.api.notifications.Client;
import org.jvalue.ceps.api.notifications.GcmClient;
import org.jvalue.commons.couchdb.DbConnectorFactory;
import org.jvalue.commons.couchdb.RepositoryAdapter;

import java.util.HashMap;
import java.util.List;

public final class ClientRepositoryTest extends AbstractRepositoryAdapterTest<Client> {

	@Override
	protected RepositoryAdapter<?, ?, Client> doCreateAdapter(DbConnectorFactory connectorFactory) {
		return new ClientRepository(connectorFactory.createConnector(getClass().getSimpleName(), true));
	}


	@Override
	protected Client doCreateValue(String id, String data) {
		return new GcmClient(id, data, "someAdapterId", new HashMap<String, Object>(), "someUserId");
	}


	@Test
	public void testFindByDeviceId() {
		Client client1 = new GcmClient("someId1", "someDevice1", "someAdapter", new HashMap<String, Object>(), "someUserId");
		Client client2 = new GcmClient("someId2", "someDevice1", "someAdapter", new HashMap<String, Object>(), "someUserId");
		Client client3 = new GcmClient("someId3", "someDevice2", "someAdapter", new HashMap<String, Object>(), "someUserId");

		repository.add(client1);
		repository.add(client2);
		repository.add(client3);

		List<Client> result = ((ClientRepository) repository).findByDeviceId("someDevice1");
		Assert.assertEquals(2, result.size());
		Assert.assertTrue(result.contains(client1));
		Assert.assertTrue(result.contains(client2));

		result = ((ClientRepository) repository).findByDeviceId("someDevice2");
		Assert.assertEquals(1, result.size());
		Assert.assertTrue(result.contains(client3));
	}

}
