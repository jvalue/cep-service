package org.jvalue.ceps.rest.data;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.jvalue.ceps.data.DataManager;
import org.jvalue.ceps.rest.RestApi;
import org.jvalue.ceps.utils.Assert;
import org.restlet.Restlet;


public final class DataRestApi implements RestApi {

	private final Map<String, Restlet> routes;

	public DataRestApi(DataManager manager) {
		Assert.assertNotNull(manager);

		Map<String, Restlet> routes = new HashMap<String, Restlet>();
		routes.put(OdsRestHook.URL_NOTIFY_SOURCE_CHANGED, new NewDataRestlet(manager));

		this.routes = Collections.unmodifiableMap(routes);
	}


	@Override
	public Map<String, Restlet> getRoutes() {
		return routes;
	}

}
