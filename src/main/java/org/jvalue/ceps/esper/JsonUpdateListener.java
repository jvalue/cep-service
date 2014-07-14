package org.jvalue.ceps.esper;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;


public interface JsonUpdateListener{

	public void onNewEvents(List<JsonNode> newEvents, List<JsonNode> oldEvents);

}
