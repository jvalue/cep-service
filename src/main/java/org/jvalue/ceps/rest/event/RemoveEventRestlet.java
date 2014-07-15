package org.jvalue.ceps.rest.event;

import org.jvalue.ceps.event.EventManager;
import org.restlet.Response;
import org.restlet.data.Method;


final class RemoveEventRestlet extends BaseEventRestlet {

	protected RemoveEventRestlet(EventManager manager) {
		super(manager);
	}


	@Override
	protected void doGet(String eventId, Response response) {
		onInvalidMethod(response, Method.GET);
	}

	
	@Override
	protected void doPost(String eventId, Response response) {
		try {
			manager.removeEvent(eventId);
			onSuccess(response);
		} catch (Exception e) {
			onInvalidRequest(response, "unknown event \"" + eventId + "\"");
			return;
		} 
	}

}
