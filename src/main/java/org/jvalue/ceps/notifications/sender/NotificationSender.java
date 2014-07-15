package org.jvalue.ceps.notifications.sender;

import java.util.List;

import org.jvalue.ceps.notifications.clients.Client;

import com.fasterxml.jackson.databind.JsonNode;


public abstract class NotificationSender<C extends Client> {

	public abstract void sendEventUpdate(
			C client, 
			String eventId,
			List<JsonNode> newEvents, 
			List<JsonNode> oldEvents);

}
