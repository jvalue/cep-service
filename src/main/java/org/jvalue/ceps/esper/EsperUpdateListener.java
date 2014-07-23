package org.jvalue.ceps.esper;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.jvalue.ceps.utils.Assert;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.EventType;
import com.espertech.esper.client.UpdateListener;
import com.espertech.esper.client.util.JSONEventRenderer;
import com.espertech.esper.client.util.JSONRenderingOptions;
import com.espertech.esper.event.util.JSONRendererImpl;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


final class EsperUpdateListener implements UpdateListener {

	private static final ObjectMapper mapper = new ObjectMapper();
	static {
		mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
	}


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

			EventType eventType = event.getEventType();
			JSONEventRenderer renderer = new JSONRendererImpl(eventType, new JSONRenderingOptions());
			String json = renderer.render("eventData", event);

			try {
				JsonNode node = mapper.readTree(json);
				jsonList.add(node);
			} catch (IOException ioe) {
				throw new RuntimeException(ioe);
			}

		}
		return jsonList;
	}

}
