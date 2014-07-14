package org.jvalue.ceps.esper;

import java.util.LinkedList;
import java.util.List;

import org.jvalue.ceps.utils.Assert;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


final class EsperUpdateListener implements UpdateListener {

	private static final ObjectMapper mapper = new ObjectMapper();

	private final JsonUpdateListener listener;


	public EsperUpdateListener(JsonUpdateListener listener) {
		Assert.assertNotNull(listener);
		this.listener = listener;
	}


	@Override
	public void update(EventBean[] newEvents, EventBean[] oldEvents) {
		listener.onNewEvents(toJson(newEvents), toJson(oldEvents));
	}


	private List<JsonNode> toJson(EventBean[] events) {
		List<JsonNode> jsonList = new LinkedList<JsonNode>();
		if (events == null) return jsonList;

		for (EventBean event : events) {
			jsonList.add(mapper.valueToTree(event.getUnderlying()));
		}
		return jsonList;
	}

}
