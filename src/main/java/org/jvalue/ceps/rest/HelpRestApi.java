package org.jvalue.ceps.rest;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jvalue.ceps.utils.Assert;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.MediaType;
import org.restlet.data.Method;


final class HelpRestApi implements RestApi {
	
	private static final String URL_API = "/api";

	private Map<String, Restlet> routes;


	public HelpRestApi(final List<String> apis) {
		Assert.assertNotNull(apis);

		List<String> apisCopy = new LinkedList<String>(apis);
		apisCopy.add(URL_API);
		Collections.sort(apisCopy);

		final StringBuilder builder = new StringBuilder();
		for (String api : apisCopy) {
			builder.append(api);
			builder.append("\n");
		}

		Map<String, Restlet> routes = new HashMap<String, Restlet>();
		routes.put(
				URL_API,
				new BaseRestlet() {

					@Override
					protected void doGet(Request request, Response response) {
						response.setEntity(builder.toString(), MediaType.TEXT_PLAIN);
						onSuccess(response);
					}


					@Override
					protected void doPost(Request request, Response response) {
						onInvalidMethod(response, Method.POST);
					}
				});
		this.routes = Collections.unmodifiableMap(routes);
	}


	@Override
	public Map<String, Restlet> getRoutes() {
		return routes;
	}

}
