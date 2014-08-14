package org.jvalue.ceps.main;

import org.restlet.Component;
import org.restlet.data.Protocol;


/** Called when running gradle run. */
public final class MainMethod {

	private static final String
		localOdsServerName = "http://localhost:8182",
		localCepsServerName = "http://localhost:8183";


	public static void main(String[] args) {
		try {
			Component component = new Component();
			component.getServers().add(Protocol.HTTP, 8183);
			CepApplication app = new CepApplication();
			app.setOdsServerName(localOdsServerName);
			app.setCepsServerName(localCepsServerName);
			component.getDefaultHost().attach(app);
			component.start();
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

}
