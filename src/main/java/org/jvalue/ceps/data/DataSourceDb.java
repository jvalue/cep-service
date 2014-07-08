package org.jvalue.ceps.data;

import java.util.LinkedList;
import java.util.List;

import org.jvalue.ceps.db.DbAccessor;
import org.jvalue.ceps.db.DbAccessorFactory;
import org.jvalue.ceps.utils.Assert;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


final class DataSourceDb {

	private static final String DB_NAME = "odsSources";

	private static DataSourceDb instance;

	public static DataSourceDb getInstance() {
		if (instance == null) instance = new DataSourceDb();
		return instance;
	}


	private final ObjectMapper mapper = new ObjectMapper();
	private final DbAccessor dbAccessor;

	private DataSourceDb() {
		this.dbAccessor = DbAccessorFactory.getCouchDbAccessor(DB_NAME);
	}


	public void add(DataSource source) {
		Assert.assertNotNull(source);
		dbAccessor.insert(mapper.valueToTree(source));
	}


	public void remove(DataSource source) {
		Assert.assertNotNull(source);
		dbAccessor.delete(mapper.valueToTree(source));
	}


	public List<DataSource> getAll() {
		List<JsonNode> jsonObjects = dbAccessor.getAll();
		List<DataSource> sources = new LinkedList<DataSource>();
		for (JsonNode json : jsonObjects) {
			try {
				sources.add(mapper.treeToValue(json, DataSource.class));
			} catch (JsonProcessingException jpe) {
				throw new IllegalStateException(jpe);
			}
		}
		return sources;
	}

}
