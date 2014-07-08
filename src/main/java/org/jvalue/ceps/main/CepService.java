package org.jvalue.ceps.main;

import org.jvalue.ceps.rest.RestletApplication;
import org.restlet.Application;
import org.restlet.Component;
import org.restlet.data.Protocol;


public final class CepService {

	private static final int PORT = 8183;


	public static void main(String[] args) {
		try {
			Component component = new Component();
			component.getServers().add(Protocol.HTTP, PORT);
			Application app = new RestletApplication();
			component.getDefaultHost().attach(app);
			component.start();
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}


}
