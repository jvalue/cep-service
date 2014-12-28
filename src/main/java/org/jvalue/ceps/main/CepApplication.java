package org.jvalue.ceps.main;

import org.jvalue.ceps.event.EventGarbageCollector;
import org.jvalue.ceps.event.EventManager;
import org.jvalue.ceps.utils.Assert;


public final class CepApplication {

	private String cepsServerName = "http://faui2o2f.cs.fau.de:8080/cep-service";
	private String odsServerName = "http://faui2o2f.cs.fau.de:8080/open-data-service";


	// private final List<RestApi> apis = new LinkedList<RestApi>();

	public CepApplication() {
		/*
		apis.add(new DataRestApi(DataManager.getInstance()));
		// apis.add(new NotificationRestApi(NotificationManager.getInstance(), new EplAdapterManager()));
		// apis.add(new EventRestApi(EventManager.getInstance()));
		apis.add(new DebugRestApi(
					NotificationManager.getInstance(),
					DataManager.getInstance(),
					EventManager.getInstance()));

		List<String> apiCalls = new LinkedList<String>();
		for (RestApi api : apis) apiCalls.addAll(api.getRoutes().keySet());
		apis.add(new HelpRestApi(apiCalls));
		*/
	}


	public void setCepsServerName(String cepsServerName) {
		Assert.assertNotNull(cepsServerName);
		this.cepsServerName = cepsServerName;
	}


	public void setOdsServerName(String odsServerName) {
		Assert.assertNotNull(odsServerName);
		this.odsServerName = odsServerName;
	}


	public void createInboundRoot() {
		/*
		Router router = new Router(getContext());

		for (RestApi api : apis) attachRoutes(router, api);
		router.attachDefault(new DefaultRestlet());

		return router;
		*/
	}


	/*
	private void attachRoutes(Router router, RestApi restApi) {
		for (Map.Entry<String, Restlet> entry : restApi.getRoutes().entrySet()) {
			router.attach(entry.getKey(), entry.getValue());
		}
	}
	*/


	public void start() throws Exception {
		startSourceMonitoring();
		startGarbageCollection();
	}


	private void startSourceMonitoring() {
		/*
		try {
			DataSource source = new DataSource(
					"de-pegelonline",
					new URL(odsServerName),
					"ods/de/pegelonline/stations/$class");
			DataManager manager = DataManager.getInstance();

			if (manager.isBeingMonitored(source)) return;

			manager.startMonitoring(
					source,
					cepsServerName + OdsRestHook.URL_DATA,
					OdsRestHook.PARAM_SOURCE);

		} catch (RestException | MalformedURLException e) {
			throw new IllegalStateException(e);
		}
		*/
	}


	private static void startGarbageCollection() {
		// trash all events older than 3 hours (clients arent using them currently anyways ...)
		EventManager eventManager = EventManager.getInstance();
		EventGarbageCollector collector = new EventGarbageCollector(eventManager, 3600000, 3600000);
		collector.start();

		/*
		// try removing old clients every 3 days (exact removal interval depends on client type)
		NotificationManager notificationManager = NotificationManager.getInstance();
		org.jvalue.ceps.notifications.garbage.GarbageCollector gcmCollector = new GcmGarbageCollector(new GcmUtils("/googleApi.key"));
		GarbageCollectorMapper mapper = new GarbageCollectorMapper(gcmCollector);
		new GarbageCollectorManager(notificationManager, mapper, 259200000).startCollection();
		*/
	}

}
