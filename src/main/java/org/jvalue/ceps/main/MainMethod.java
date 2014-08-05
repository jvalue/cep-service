package org.jvalue.ceps.main;

import org.restlet.Application;
import org.restlet.Component;
import org.restlet.data.Protocol;


public final class MainMethod {

	public static void main(String[] args) {
		try {
			Component component = new Component();
			component.getServers().add(Protocol.HTTP, CepApplication.SERVER_PORT);
			Application app = new CepApplication();
			component.getDefaultHost().attach(app);
			component.start();
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

}
