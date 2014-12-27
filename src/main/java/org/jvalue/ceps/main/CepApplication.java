package org.jvalue.ceps.main;

import org.jvalue.ceps.adapter.EplAdapterManager;
import org.jvalue.ceps.data.DataManager;
import org.jvalue.ceps.data.DataSource;
import org.jvalue.ceps.event.EventGarbageCollector;
import org.jvalue.ceps.event.EventManager;
import org.jvalue.ceps.notifications.NotificationManager;
import org.jvalue.ceps.rest.DefaultRestlet;
import org.jvalue.ceps.rest.HelpRestApi;
import org.jvalue.ceps.rest.RestApi;
import org.jvalue.ceps.rest.data.DataRestApi;
import org.jvalue.ceps.rest.data.OdsRestHook;
import org.jvalue.ceps.rest.debug.DebugRestApi;
import org.jvalue.ceps.rest.event.EventRestApi;
import org.jvalue.ceps.rest.notifications.NotificationRestApi;
import org.jvalue.ceps.utils.Assert;
import org.jvalue.ceps.utils.RestException;
import org.jvalue.ceps.utils.Restoreable;
import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public final class CepApplication extends Application {

	private String cepsServerName = "http://faui2o2f.cs.fau.de:8080/cep-service";
	private String odsServerName = "http://faui2o2f.cs.fau.de:8080/open-data-service";


	private final List<RestApi> apis = new LinkedList<RestApi>();

	public CepApplication() {
		apis.add(new DataRestApi(DataManager.getInstance()));
		apis.add(new NotificationRestApi(NotificationManager.getInstance(), new EplAdapterManager()));
		apis.add(new EventRestApi(EventManager.getInstance()));
		apis.add(new DebugRestApi(
					NotificationManager.getInstance(),
					DataManager.getInstance(),
					EventManager.getInstance()));

		List<String> apiCalls = new LinkedList<String>();
		for (RestApi api : apis) apiCalls.addAll(api.getRoutes().keySet());
		apis.add(new HelpRestApi(apiCalls));
	}


	public void setCepsServerName(String cepsServerName) {
		Assert.assertNotNull(cepsServerName);
		this.cepsServerName = cepsServerName;
	}


	public void setOdsServerName(String odsServerName) {
		Assert.assertNotNull(odsServerName);
		this.odsServerName = odsServerName;
	}


	@Override
	public Restlet createInboundRoot() {
		Router router = new Router(getContext());

		for (RestApi api : apis) attachRoutes(router, api);
		router.attachDefault(new DefaultRestlet());

		return router;
	}


	private void attachRoutes(Router router, RestApi restApi) {
		for (Map.Entry<String, Restlet> entry : restApi.getRoutes().entrySet()) {
			router.attach(entry.getKey(), entry.getValue());
		}
	}


	@Override
	public void start() throws Exception {
		super.start();
		restoreState();
		startSourceMonitoring();
		startGarbageCollection();
	}


	private void restoreState() {
		List<Restoreable> restoreables = Arrays.asList(
				DataManager.getInstance(),
				NotificationManager.getInstance());

		for (Restoreable restoreable : restoreables) restoreable.restoreState();
	}


	private void startSourceMonitoring() {
		try {
			DataSource source = new DataSource(
					"de-pegelonline",
					new URL(odsServerName),
					"ods/de/pegelonline/stations/$class");
			DataManager manager = DataManager.getInstance();

			if (manager.isBeingMonitored(source)) return;

			manager.startMonitoring(
					source,
					cepsServerName + OdsRestHook.URL_NOTIFY_SOURCE_CHANGED,
					OdsRestHook.PARAM_SOURCE);

		} catch (RestException | MalformedURLException e) {
			throw new IllegalStateException(e);
		}
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
