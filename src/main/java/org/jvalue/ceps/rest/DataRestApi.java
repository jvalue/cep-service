package org.jvalue.ceps.rest;

import java.net.URLDecoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jvalue.ceps.data.DataManager;
import org.jvalue.ceps.rest.restlet.BaseRestlet;
import org.jvalue.ceps.utils.Assert;
import org.jvalue.ceps.utils.Log;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.Method;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


final class DataRestApi implements RestApi {

	private static final ObjectMapper mapper = new ObjectMapper();


	private Map<String, Restlet> routes;

	public DataRestApi(final DataManager manager) {
		Assert.assertNotNull(manager);

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
						try {
							String sourceId = getParameter(request, OdsRestHook.PARAM_SOURCE);
							String rawString = request.getEntity().getText();

							String jsonString = URLDecoder.decode(rawString, "UTF-8");
							JsonNode data = mapper.readTree(jsonString);

							manager.onSourceChanged(sourceId, data);

						} catch (Exception e) {
							Log.error("retreiving data from ods failed", e);
						}
					}
				});

		this.routes = Collections.unmodifiableMap(routes);
	}


	@Override
	public Map<String, Restlet> getRoutes() {
		return routes;
	}

}
