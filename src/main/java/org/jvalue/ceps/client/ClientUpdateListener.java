package org.jvalue.ceps.client;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;


public interface ClientUpdateListener {

	public void onNewEvents(String clientId, List<JsonNode> newEvents, List<JsonNode> oldEvents);

}
