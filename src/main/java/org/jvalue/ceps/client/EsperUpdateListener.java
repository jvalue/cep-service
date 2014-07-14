package org.jvalue.ceps.client;

import java.util.List;

import org.jvalue.ceps.esper.JsonUpdateListener;
import org.jvalue.ceps.utils.Assert;

import com.fasterxml.jackson.databind.JsonNode;


final class EsperUpdateListener implements JsonUpdateListener {

	private final String clientId;
	private final ClientUpdateListener listener;


	public EsperUpdateListener(String clientId, ClientUpdateListener listener) {
		Assert.assertNotNull(clientId, listener);
		this.clientId = clientId;
		this.listener = listener;
	}


	@Override
	public void onNewEvents(List<JsonNode> newEvents, List<JsonNode> oldEvents) {
		listener.onNewEvents(clientId, newEvents, oldEvents);
	}

}
