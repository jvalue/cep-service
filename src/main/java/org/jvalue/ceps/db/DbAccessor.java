package org.jvalue.ceps.db;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;


public interface DbAccessor {

	public void insert(JsonNode json);
	public void update(JsonNode json);
	public void delete(JsonNode json);
	public List<JsonNode> getAll();
	public List<JsonNode> query(String docId, String viewName, String key);

}
