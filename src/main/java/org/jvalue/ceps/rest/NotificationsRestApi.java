package org.jvalue.ceps.rest;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.jvalue.ceps.notifications.NotificationManager;
import org.jvalue.ceps.rest.restlet.RestletFactory;
import org.jvalue.ceps.utils.Assert;
import org.restlet.Restlet;


final class NotificationsRestApi implements RestApi {

	private static final String 
		PATH_PREFIX = "/cep",
		PATH_REGISTER_GCM = PATH_PREFIX + "/registerGcm",
		PATH_UNREGISTER = PATH_PREFIX + "/unregister";


	private Map<String, Restlet> routes;

	public NotificationsRestApi(final NotificationManager manager) {
		Assert.assertNotNull(manager);

		Map<String, Restlet> routes = new HashMap<String, Restlet>();

		routes.put(PATH_REGISTER_GCM, RestletFactory.createGcmRegisterRestlet());
		routes.put(PATH_UNREGISTER, RestletFactory.createUnregisterRestlet());

		this.routes = Collections.unmodifiableMap(routes);
	}


	@Override
	public Map<String, Restlet> getRoutes() {
		return routes;
	}

}
