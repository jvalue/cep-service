package org.jvalue.ceps.db;


import org.ektorp.CouchDbInstance;
import org.ektorp.support.CouchDbDocument;
import org.ektorp.support.CouchDbRepositorySupport;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public abstract class AbstractRepositoryTest_Old<R extends CouchDbRepositorySupport<T>, T extends CouchDbDocument> {

	private final String databaseName;
	private final CouchDbInstance couchdbInstance;

	protected R repository;

	public AbstractRepositoryTest_Old(String databaseName) {
		this.databaseName = databaseName;
		this.couchdbInstance = DbFactory.createCouchDbInstance();
	}


	@Before
	public final void createDatabase() {
		this.repository = doCreateDatabase(couchdbInstance, databaseName);
	}


	@After
	public final void deleteDatabase() {
		couchdbInstance.deleteDatabase(databaseName);
	}


	@Test
	public void testGetAll() {
		// insert data
		Map<String, T> itemsMap = doSetupDataItems();
		for (T item : itemsMap.values()) repository.add(item);

		// fetch and check data
		List<T> receivedItems = repository.getAll();
		Assert.assertEquals(itemsMap.size(), receivedItems.size());
		for (T item : receivedItems) Assert.assertNotNull(itemsMap.remove(doGetIdForItem(item)));
	}


	protected abstract R doCreateDatabase(CouchDbInstance couchdbInstance, String databaseName);


	protected abstract Map<String, T> doSetupDataItems();


	protected abstract String doGetIdForItem(T item);

}
