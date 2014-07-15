package org.jvalue.ceps.rest;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.jvalue.ceps.rest.restlet.RestletFactory;
import org.restlet.Restlet;


final class EventRestApi implements RestApi {

	private static final String 
		PATH_PREFIX = "/event",
		PATH_EVENT_GET = PATH_PREFIX + "/get",
		PATH_EVENT_REMOVE = PATH_PREFIX + "/remove";


	private Map<String, Restlet> routes;

	public EventRestApi() {
		Map<String, Restlet> routes = new HashMap<String, Restlet>();

		routes.put(PATH_EVENT_GET, RestletFactory.createFetchEventRestlet());
		routes.put(PATH_EVENT_REMOVE, RestletFactory.createRemoveEventRestlet());

		this.routes = Collections.unmodifiableMap(routes);
	}


	@Override
	public Map<String, Restlet> getRoutes() {
		return routes;
	}

}
