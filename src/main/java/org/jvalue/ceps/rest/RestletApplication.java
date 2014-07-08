package org.jvalue.ceps.rest;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;


public final class RestletApplication extends Application {


	@Override
	public Restlet createInboundRoot() {
		Router router = new Router(getContext());
		return router;
	}


}
