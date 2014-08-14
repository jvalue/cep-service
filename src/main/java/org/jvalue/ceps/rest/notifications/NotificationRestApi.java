package org.jvalue.ceps.rest.notifications;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.jvalue.ceps.notifications.NotificationManager;
import org.jvalue.ceps.rest.RestApi;
import org.jvalue.ceps.rest.rules.ClientAdapter;
import org.jvalue.ceps.rest.rules.ClientAdapterManager;
import org.jvalue.ceps.utils.Assert;
import org.restlet.Restlet;


public final class NotificationRestApi implements RestApi {

	private static final String 
		PATH_PREFIX = "/cep",
		PATH_REGISTER = PATH_PREFIX + "/register",
		PATH_UNREGISTER = PATH_PREFIX + "/unregister";


	private Map<String, Restlet> routes;

	public NotificationRestApi(
			NotificationManager notificationManager, 
			ClientAdapterManager adapterManager) {

		Assert.assertNotNull(notificationManager, adapterManager);

		Map<String, Restlet> routes = new HashMap<String, Restlet>();

		// unregister is config independent
		routes.put(PATH_UNREGISTER, new UnregisterRestlet(notificationManager));

		// configurable rules
		for (Map.Entry<String, ClientAdapter> entry : adapterManager.getAdapters().entrySet()) {
			// enable gcm registration for all
			routes.put(
					PATH_REGISTER + entry.getKey(), 
					new GcmRegisterRestlet(notificationManager, entry.getValue()));
		}

		this.routes = Collections.unmodifiableMap(routes);
	}


	@Override
	public Map<String, Restlet> getRoutes() {
		return routes;
	}

}
