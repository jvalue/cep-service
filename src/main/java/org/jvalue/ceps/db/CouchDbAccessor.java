package org.jvalue.ceps.db;

import java.util.LinkedList;
import java.util.List;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.ViewQuery;
import org.ektorp.ViewResult;
import org.ektorp.ViewResult.Row;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbConnector;
import org.ektorp.impl.StdCouchDbInstance;
import org.jvalue.ceps.utils.Assert;

import com.fasterxml.jackson.databind.JsonNode;


final class CouchDbAccessor implements DbAccessor {

	private final String dbName;
	private CouchDbConnector dbConnector;


	public CouchDbAccessor(String dbName) {
		Assert.assertNotNull(dbName);
		this.dbName = dbName;
	}


	@Override
	public void insert(JsonNode json) {
		Assert.assertNotNull(json);
		checkDbState();

		dbConnector.create(json);
	}


	@Override
	public void update(JsonNode json) {
		Assert.assertNotNull(json);
		checkDbState();

		dbConnector.update(json);
	}


	@Override
	public void delete(JsonNode json) {
		Assert.assertNotNull(json);
		checkDbState();

		dbConnector.delete(json);
	}


	@Override
	public List<JsonNode> getAll() { 
		checkDbState();

		ViewQuery query = new ViewQuery().allDocs().includeDocs(true);
		return viewResultToList(dbConnector.queryView(query));
	}


	@Override
	public List<JsonNode> query(String docId, String viewName, String key) {
		Assert.assertNotNull(docId, viewName);
		checkDbState();

		ViewQuery query = new ViewQuery().designDocId(docId).viewName(viewName).key(key);
		return viewResultToList(dbConnector.queryView(query));
	}


	private List<JsonNode> viewResultToList(ViewResult result) {
		List<JsonNode> retList = new LinkedList<JsonNode>();
		for (Row row : result.getRows()) {
			retList.add(row.getDocAsNode());
		}
		return retList;
	}


	private void checkDbState() {
		if (dbConnector != null) return;

		// create connector and db
		HttpClient client = new StdHttpClient.Builder().build();
		CouchDbInstance dbInstance = new StdCouchDbInstance(client);
		this.dbConnector = new StdCouchDbConnector(dbName, dbInstance);
		if (!dbInstance.checkIfDbExists(dbName)) dbInstance.createDatabase(dbName);
	}

}
