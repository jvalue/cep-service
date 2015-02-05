package org.jvalue.ceps.esper;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;


public interface EventUpdateListener {

	public void onNewEvents(String registrationId, List<JsonNode> newEvents, List<JsonNode> oldEvents);

}
