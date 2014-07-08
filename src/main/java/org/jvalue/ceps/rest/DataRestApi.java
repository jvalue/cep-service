package org.jvalue.ceps.rest;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jvalue.ceps.data.DataManager;
import org.jvalue.ceps.data.OdsRestHook;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.Method;


final class DataRestApi implements RestApi {

	private static Map<String, Restlet> routes;
	static {
		Map<String, Restlet> routes = new HashMap<String, Restlet>();
		Set<String> requiredParams = new HashSet<String>();
		requiredParams.add(OdsRestHook.PARAM_SOURCE);
		Set<String> optionalParams = new HashSet<String>();

		routes.put(
				OdsRestHook.URL_NOTIFY_SOURCE_CHANGED,
				new BaseRestlet(requiredParams, optionalParams) {

					@Override
					public void doGet(Request request, Response response) {
						onInvalidMethod(response, Method.GET);
					}


					@Override
					public void doPost(Request request, Response response) {
						String sourceId = getParameter(request, OdsRestHook.PARAM_SOURCE);
						DataManager.getInstance().onSourceChanged(sourceId);
					}
				});

		DataRestApi.routes = Collections.unmodifiableMap(routes);
	}


	@Override
	public Map<String, Restlet> getRoutes() {
		return routes;
	}

}
