package org.jvalue.ceps.data;

import java.util.LinkedList;
import java.util.List;

import org.jvalue.ceps.db.DbAccessor;
import org.jvalue.ceps.utils.Assert;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


final class DataSourceDb {

	private final ObjectMapper mapper = new ObjectMapper();
	private final DbAccessor dbAccessor;

	public DataSourceDb(DbAccessor dbAccessor) {
		Assert.assertNotNull(dbAccessor);
		this.dbAccessor = dbAccessor;
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
