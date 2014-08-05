package org.jvalue.ceps.rest.debug;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.jvalue.ceps.data.DataManager;
import org.jvalue.ceps.notifications.NotificationManager;
import org.jvalue.ceps.rest.RestApi;
import org.jvalue.ceps.utils.Assert;
import org.restlet.Restlet;


public final class DebugRestApi implements RestApi {

	private static final String 
		PATH_PREFIX = "/debug",
		PATH_CLIENTS = PATH_PREFIX + "/clients",
		PATH_SOURCES = PATH_PREFIX + "/data";


	private Map<String, Restlet> routes;

	public DebugRestApi(
			NotificationManager notificationManager,
			DataManager dataManager) {

		Assert.assertNotNull(notificationManager, dataManager);

		Map<String, Restlet> routes = new HashMap<String, Restlet>();

		routes.put(PATH_CLIENTS, new ClientsRestlet(notificationManager));
		routes.put(PATH_SOURCES, new SourceRestlet(dataManager));

		this.routes = Collections.unmodifiableMap(routes);
	}


	@Override
	public Map<String, Restlet> getRoutes() {
		return routes;
	}

}
