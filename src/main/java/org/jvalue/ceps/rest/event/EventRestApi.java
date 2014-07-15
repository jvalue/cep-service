package org.jvalue.ceps.rest.event;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.jvalue.ceps.event.EventManager;
import org.jvalue.ceps.rest.RestApi;
import org.restlet.Restlet;


public final class EventRestApi implements RestApi {

	private static final String 
		PATH_PREFIX = "/event",
		PATH_EVENT_GET = PATH_PREFIX + "/get",
		PATH_EVENT_REMOVE = PATH_PREFIX + "/remove";


	private final Map<String, Restlet> routes;

	public EventRestApi(EventManager manager) {
		Map<String, Restlet> routes = new HashMap<String, Restlet>();

		routes.put(PATH_EVENT_GET, new FetchEventRestlet(manager));
		routes.put(PATH_EVENT_REMOVE, new RemoveEventRestlet(manager));

		this.routes = Collections.unmodifiableMap(routes);
	}


	@Override
	public Map<String, Restlet> getRoutes() {
		return routes;
	}

}
