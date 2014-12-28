package org.jvalue.ceps.rest.notifications;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import org.jvalue.ceps.adapter.AdapterModule;
import org.jvalue.ceps.adapter.EplAdapter;
import org.jvalue.ceps.notifications.NotificationManager;
import org.jvalue.ceps.rest.RestApi;
import org.restlet.Restlet;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public final class NotificationRestApi implements RestApi {

	private static final String 
		PATH_PREFIX = "/cep",
		PATH_REGISTER = PATH_PREFIX + "/register",
		PATH_UNREGISTER = PATH_PREFIX + "/unregister";


	private Map<String, Restlet> routes;

	@Inject
	public NotificationRestApi(
			NotificationManager notificationManager, 
			@Named(AdapterModule.ADAPTER_PEGELONLINE) EplAdapter eplAdapter) {


		Map<String, Restlet> routes = new HashMap<String, Restlet>();

		// unregister is config independent
		routes.put(PATH_UNREGISTER, new UnregisterRestlet(notificationManager));

		// configure rules
		routes.put(PATH_REGISTER + "/pegelonline", new GcmRegisterRestlet(notificationManager, eplAdapter));
		this.routes = Collections.unmodifiableMap(routes);
	}


	@Override
	public Map<String, Restlet> getRoutes() {
		return routes;
	}

}
