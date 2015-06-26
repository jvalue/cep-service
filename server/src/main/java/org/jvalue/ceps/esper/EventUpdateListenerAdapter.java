package org.jvalue.ceps.esper;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.jvalue.ceps.utils.Assert;


/**
 * Forwards a new events notification to further listeners.
 */
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
		listener.onNewEvents(registrationId);
	}

}
