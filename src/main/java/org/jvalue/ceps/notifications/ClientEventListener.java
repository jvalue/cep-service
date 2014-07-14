package org.jvalue.ceps.notifications;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;


interface ClientEventListener<C extends Client> {

	public void onNewEvents(
			C client, 
			List<JsonNode> newEvents, 
			List<JsonNode> oldEvents);

}
