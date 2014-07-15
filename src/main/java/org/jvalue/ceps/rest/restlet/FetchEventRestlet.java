package org.jvalue.ceps.rest.restlet;

import org.jvalue.ceps.event.Event;
import org.jvalue.ceps.event.EventManager;
import org.restlet.Response;
import org.restlet.data.MediaType;
import org.restlet.data.Method;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


final class FetchEventRestlet extends BaseEventRestlet {

	private static final ObjectMapper mapper = new ObjectMapper();

	public FetchEventRestlet(EventManager manager) {
		super(manager);
	}

	@Override
	protected void doGet(String eventId, Response response) {
		try {
			Event event = manager.getEvent(eventId);
			JsonNode json = mapper.valueToTree(event);
			response.setEntity(json.toString(), MediaType.APPLICATION_JSON);
			onSuccess(response);
		} catch (Exception e) {
			onInvalidRequest(response, "unknown event \"" + eventId + "\"");
		}
	}


	@Override
	protected void doPost(String eventId, Response response) {
		onInvalidMethod(response, Method.POST);
	}

}
