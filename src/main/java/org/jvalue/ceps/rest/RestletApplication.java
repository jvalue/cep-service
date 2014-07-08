package org.jvalue.ceps.rest;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;


public final class RestletApplication extends Application {

	private final List<RestApi> apis = new LinkedList<RestApi>();

	public RestletApplication() {
		apis.add(new DataRestApi());

		List<String> apiCalls = new LinkedList<String>();
		for (RestApi api : apis) apiCalls.addAll(api.getRoutes().keySet());
		apis.add(new HelpRestApi(apiCalls));
	}


	@Override
	public Restlet createInboundRoot() {
		Router router = new Router(getContext());

		for (RestApi api : apis) attachRoutes(router, api);
		router.attachDefault(new DefaultReslet());

		return router;
	}


	private void attachRoutes(Router router, RestApi restApi) {
		for (Map.Entry<String, Restlet> entry : restApi.getRoutes().entrySet()) {
			router.attach(entry.getKey(), entry.getValue());
		}
	}


}
