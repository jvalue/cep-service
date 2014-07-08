package org.jvalue.ceps.rest;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jvalue.ceps.data.DataManager;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.Method;


final class DataRestApi implements RestApi {

	private static final String URL_NOTIFY_SOURCE_CHANGED = "/data";
	private static final String PARAM_SOURCE = "source";

	private static final Map<String, Restlet> routes = new HashMap<String, Restlet>();
	static {
		Set<String> requiredParams = new HashSet<String>();
		requiredParams.add(PARAM_SOURCE);
		Set<String> optionalParams = new HashSet<String>();

		routes.put(
				URL_NOTIFY_SOURCE_CHANGED,
				new BaseRestlet(requiredParams, optionalParams) {

					@Override
					public void doGet(Request request, Response response) {
						onInvalidMethod(response, Method.GET);
					}


					@Override
					public void doPost(Request request, Response response) {
						String sourceId = getParameter(request, PARAM_SOURCE);
						DataManager.getInstance().onSourceChanged(sourceId);
					}
				});
	}




	@Override
	public Map<String, Restlet> getRoutes() {
		return routes;
	}

}
