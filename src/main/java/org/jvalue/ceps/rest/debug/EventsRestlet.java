package org.jvalue.ceps.rest.debug;

import java.util.LinkedList;
import java.util.List;

import org.jvalue.ceps.event.Event;
import org.jvalue.ceps.event.EventManager;
import org.jvalue.ceps.rest.BaseRestlet;
import org.jvalue.ceps.rest.RestletResult;
import org.jvalue.ceps.utils.Assert;
import org.restlet.Request;

import com.fasterxml.jackson.databind.ObjectMapper;


final class EventsRestlet extends BaseRestlet {

	private static final ObjectMapper mapper = new ObjectMapper();

	private final EventManager manager;

	protected EventsRestlet(EventManager manager) {
		Assert.assertNotNull(manager);
		this.manager = manager;
	}


	@Override
	protected final RestletResult doGet(Request request) {
		List<String> eventIds = new LinkedList<String>();
		for (Event event : manager.getAll()) {
			eventIds.add(event.getEventId());
		}
		return RestletResult.newSuccessResult(mapper.valueToTree(eventIds));
	}

}
