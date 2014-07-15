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
	private final String eplStmtId;


	public EsperUpdateListener(JsonUpdateListener listener, String eplStmtId) {
		Assert.assertNotNull(listener, eplStmtId);
		this.listener = listener;
		this.eplStmtId = eplStmtId;
	}


	@Override
	public void update(EventBean[] newEvents, EventBean[] oldEvents) {
		listener.onNewEvents(eplStmtId, toJson(newEvents), toJson(oldEvents));
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
