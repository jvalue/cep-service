package org.jvalue.ceps.esper;

import java.util.Map;

import org.jvalue.ceps.utils.Assert;


final class EventDefinition {

	private final String name;
	private final Map<String, Object> schema; 

	public EventDefinition(String name, Map<String, Object> schema) {
		Assert.assertNotNull(name, schema);
		this.name = name;
		this.schema = schema;
	}

	public String getName() {
		return name;
	}

	public Map<String, Object> getSchema() {
		return schema;
	}

}
