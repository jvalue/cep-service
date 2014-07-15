package org.jvalue.ceps.notifications.sender;

import java.util.List;

import org.jvalue.ceps.notifications.clients.GcmClient;

import com.fasterxml.jackson.databind.JsonNode;


final class GcmSender extends NotificationSender<GcmClient> {

	@Override
	public void sendEventUpdate(
			GcmClient client, 
			String eventId,
			List<JsonNode> newEvents, 
			List<JsonNode> oldEvents) {

		// lots to do there
	}

}
