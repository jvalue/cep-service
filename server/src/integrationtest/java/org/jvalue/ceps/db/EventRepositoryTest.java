package org.jvalue.ceps.db;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.jvalue.ceps.event.Event;
import org.jvalue.common.db.DbConnectorFactory;
import org.jvalue.common.db.RepositoryAdapter;

import java.util.Arrays;

public final class EventRepositoryTest extends AbstractRepositoryAdapterTest<Event> {

	@Override
	protected RepositoryAdapter<?, ?, Event> doCreateAdapter(DbConnectorFactory connectorFactory) {
		return new EventRepository(connectorFactory.createConnector(getClass().getSimpleName(), true));
	}


	@Override
	protected Event doCreateValue(String id, String data) {
		ObjectNode jsonData = new ObjectNode(JsonNodeFactory.instance);
		jsonData.put("someKey", data);
		return new Event(
				id,
				1201,
				Arrays.asList((JsonNode) jsonData),
				Arrays.asList((JsonNode) jsonData));
	}

}
