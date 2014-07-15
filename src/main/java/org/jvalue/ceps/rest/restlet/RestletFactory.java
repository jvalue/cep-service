package org.jvalue.ceps.rest.restlet;

import org.restlet.Restlet;


public final class RestletFactory {

	private RestletFactory() { }


	public static Restlet createDefaultRestlet() {
		return new DefaultRestlet();
	}

}
