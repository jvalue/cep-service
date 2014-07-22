package org.jvalue.ceps.rest.event;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.jvalue.ceps.event.EventManager;
import org.jvalue.ceps.rest.BaseRestlet;
import org.jvalue.ceps.utils.Assert;
import org.restlet.Request;


abstract class BaseEventRestlet extends BaseRestlet {

	private static final String PARAM_EVENT_ID = "eventId";
	private static final Set<String> PARAMS;
	static {
		Set<String> params = new HashSet<String>();
		params.add(PARAM_EVENT_ID);
		PARAMS = Collections.unmodifiableSet(params);
	}


	protected final EventManager manager;

	protected BaseEventRestlet(EventManager manager) {
		super(PARAMS, false);
		Assert.assertNotNull(manager);
		this.manager = manager;
	}


	protected final String getEventId(Request request) {
		return getParameter(request, PARAM_EVENT_ID);
	}

}
