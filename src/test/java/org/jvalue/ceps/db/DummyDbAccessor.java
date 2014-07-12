package org.jvalue.ceps.db;

import java.util.LinkedList;
import java.util.List;

import org.jvalue.ceps.utils.Assert;

import com.fasterxml.jackson.databind.JsonNode;


public final class DummyDbAccessor implements DbAccessor {


	private final List<JsonNode> data = new LinkedList<JsonNode>();

	@Override
	public void insert(JsonNode json) {
		Assert.assertNotNull(json);
		data.add(json);
	}


	@Override
	public void update(JsonNode json) {
		throw new UnsupportedOperationException("not supported");
	}


	@Override
	public void delete(JsonNode json) {
		Assert.assertNotNull(json);
		data.remove(json);
	}

	
	@Override
	public List<JsonNode> getAll() {
		return new LinkedList<JsonNode>(data);
	}


	@Override
	public List<JsonNode> query(String docId, String viewName, String key) {
		throw new UnsupportedOperationException("not supported");
	}

}
