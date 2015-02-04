package org.jvalue.ceps.db;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.ektorp.CouchDbInstance;
import org.jvalue.ceps.event.Event;
import org.jvalue.common.db.RepositoryAdapter;

import java.util.Arrays;

public final class EventRepositoryTest extends AbstractRepositoryAdapterTest<Event> {


	public EventRepositoryTest() {
		super(EventRepositoryTest.class.getSimpleName());
	}


	@Override
	protected RepositoryAdapter<?, ?, Event> doCreateAdapter(CouchDbInstance couchDbInstance, String databaseName) {
		return new EventRepository(couchDbInstance.createConnector(databaseName, true));
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
