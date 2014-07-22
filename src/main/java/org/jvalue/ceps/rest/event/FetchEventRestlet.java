package org.jvalue.ceps.rest.event;

import org.jvalue.ceps.event.Event;
import org.jvalue.ceps.event.EventManager;
import org.jvalue.ceps.rest.RestletResult;
import org.restlet.Request;
import org.restlet.data.Status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


final class FetchEventRestlet extends BaseEventRestlet {

	private static final ObjectMapper mapper = new ObjectMapper();

	public FetchEventRestlet(EventManager manager) {
		super(manager);
	}

	@Override
	protected RestletResult doGet(Request request) {
		String eventId = getEventId(request);
		try {
			Event event = manager.getEvent(eventId);
			JsonNode json = mapper.valueToTree(event);
			return RestletResult.newSuccessResult(json);
		} catch (Exception e) {
			return RestletResult.newErrorResult(
					Status.CLIENT_ERROR_NOT_FOUND, 
					"unknown event \"" + eventId + "\"");
		}
	}

}
