package org.jvalue.ceps.rest;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jvalue.ceps.utils.Assert;
import org.restlet.Request;
import org.restlet.Restlet;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


final class HelpRestApi implements RestApi {
	
	private static final ObjectMapper mapper = new ObjectMapper();
	
	private static final String URL_API = "/api";

	private Map<String, Restlet> routes;


	public HelpRestApi(final List<String> apis) {
		Assert.assertNotNull(apis);

		List<String> apisCopy = new LinkedList<String>(apis);
		apisCopy.add(URL_API);
		Collections.sort(apisCopy);

		final JsonNode data = mapper.valueToTree(apisCopy);

		Map<String, Restlet> routes = new HashMap<String, Restlet>();
		routes.put(
				URL_API,
				new BaseRestlet() {

					@Override
					protected RestletResult doGet(Request request) {
						return RestletResult.newSuccessResult(data);
					}

				});
		this.routes = Collections.unmodifiableMap(routes);
	}


	@Override
	public Map<String, Restlet> getRoutes() {
		return routes;
	}

}
