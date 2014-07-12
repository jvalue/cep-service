package org.jvalue.ceps.main;

import org.jvalue.ceps.data.DataManager;
import org.jvalue.ceps.data.DataSource;
import org.jvalue.ceps.rest.OdsRestHook;
import org.jvalue.ceps.rest.RestletApplication;
import org.jvalue.ceps.utils.RestException;
import org.restlet.Application;
import org.restlet.Component;
import org.restlet.data.Protocol;


public final class CepService {

	private static final String SERVER_NAME = "http://localhost";
	private static final int SERVER_PORT = 8183;

	private static final String ODS_SERVER = "http://localhost:8182";


	public static void main(String[] args) {
		startRestService();
		startSourceMonitoring();
	}


	private static void startRestService() {
		try {
			Component component = new Component();
			component.getServers().add(Protocol.HTTP, SERVER_PORT);
			Application app = new RestletApplication();
			component.getDefaultHost().attach(app);
			component.start();
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}


	private static void startSourceMonitoring() {
		DataSource source = new DataSource(
				"de-pegelonline", 
				ODS_SERVER,
				"ods/de/pegelonline/stations/$class");
		DataManager manager = DataManager.getInstance();

		if (manager.isBeingMonitored(source)) return;

		try {
			manager.startMonitoring(
					source,
					SERVER_NAME + ":" + SERVER_PORT + OdsRestHook.URL_NOTIFY_SOURCE_CHANGED,
					OdsRestHook.PARAM_SOURCE);

		} catch (RestException re) {
			throw new IllegalStateException(re);
		}
	}



}
