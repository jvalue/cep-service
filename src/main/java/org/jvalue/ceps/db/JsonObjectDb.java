package org.jvalue.ceps.db;

import java.util.LinkedList;
import java.util.List;

import org.jvalue.ceps.db.DbAccessor;
import org.jvalue.ceps.utils.Assert;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


public final class JsonObjectDb<T> {

	private static final ObjectMapper mapper = new ObjectMapper();

	private final DbAccessor dbAccessor;
	private final Class<T> objectClass;

	public JsonObjectDb(DbAccessor dbAccessor, Class<T> objectClass) {
		Assert.assertNotNull(dbAccessor, objectClass);
		this.dbAccessor = dbAccessor;
		this.objectClass = objectClass;
	}


	public void add(T object) {
		Assert.assertNotNull(object);
		dbAccessor.insert(mapper.valueToTree(object));
	}


	public void remove(T object) {
		Assert.assertNotNull(object);
		dbAccessor.delete(mapper.valueToTree(object));
	}


	public List<T> getAll() {
		List<JsonNode> jsonObjects = dbAccessor.getAll();
		List<T> objects = new LinkedList<T>();
		for (JsonNode json : jsonObjects) {
			try {
				objects.add(mapper.treeToValue(json, objectClass));
			} catch (JsonProcessingException jpe) {
				throw new IllegalStateException(jpe);
			}
		}
		return objects;
	}

}
