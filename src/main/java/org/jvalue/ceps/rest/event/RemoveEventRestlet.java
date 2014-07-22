package org.jvalue.ceps.rest.event;

import org.jvalue.ceps.event.EventManager;
import org.jvalue.ceps.rest.RestletResult;
import org.restlet.Request;
import org.restlet.data.Status;


final class RemoveEventRestlet extends BaseEventRestlet {

	protected RemoveEventRestlet(EventManager manager) {
		super(manager);
	}

	
	@Override
	protected RestletResult doPost(Request request) {
		String eventId = getEventId(request);
		try {
			manager.removeEvent(eventId);
			return RestletResult.newSuccessResult();
		} catch (Exception e) {
			return RestletResult.newErrorResult(
					Status.CLIENT_ERROR_NOT_FOUND, 
					"unknown event \"" + eventId + "\"");
		} 
	}

}
