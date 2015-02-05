package org.jvalue.ceps.esper;

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

import org.jvalue.ceps.utils.Assert;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;


final class EventUpdateListenerAdapter implements UpdateListener {

	private static final ObjectMapper mapper = new ObjectMapper();
	static {
		mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
	}


	private final EventUpdateListener listener;
	private final String registrationId;


	public EventUpdateListenerAdapter(EventUpdateListener listener, String registrationId) {
		Assert.assertNotNull(listener, registrationId);
		this.listener = listener;
		this.registrationId = registrationId;
	}


	@Override
	public void update(EventBean[] newEvents, EventBean[] oldEvents) {
		listener.onNewEvents(registrationId, toJson(newEvents), toJson(oldEvents));
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
