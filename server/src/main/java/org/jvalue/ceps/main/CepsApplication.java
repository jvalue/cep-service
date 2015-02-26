package org.jvalue.ceps.main;

import com.google.inject.Guice;
import com.google.inject.Injector;

import org.jvalue.ceps.adapter.AdapterModule;
import org.jvalue.ceps.data.DataManager;
import org.jvalue.ceps.data.DataModule;
import org.jvalue.ceps.db.DbModule;
import org.jvalue.ceps.esper.EsperModule;
import org.jvalue.ceps.event.EventGarbageCollector;
import org.jvalue.ceps.event.EventModule;
import org.jvalue.ceps.notifications.NotificationManager;
import org.jvalue.ceps.notifications.NotificationsModule;
import org.jvalue.ceps.notifications.garbage.ClientGarbageCollectorManager;
import org.jvalue.ceps.ods.OdsModule;
import org.jvalue.ceps.rest.DataApi;
import org.jvalue.ceps.rest.EplAdapterApi;
import org.jvalue.ceps.rest.EventApi;
import org.jvalue.ceps.rest.RegistrationApi;
import org.jvalue.ceps.rest.RestModule;
import org.jvalue.ceps.rest.SourcesApi;
import org.jvalue.common.rest.DbExceptionMapper;
import org.jvalue.common.rest.JsonExceptionMapper;

import javax.ws.rs.core.Context;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;


public final class CepsApplication extends Application<CepsConfig> {

	private String cepsServerName = "http://faui2o2f.cs.fau.de:8080/cep-service";
	private String odsServerName = "http://faui2o2f.cs.fau.de:8080/open-data-service";


	public static void main(String[] args) throws Exception {
		new CepsApplication().run(args);
	}


	@Override
	public String getName() {
		return "CEP Service";
	}


	@Override
	public void initialize(Bootstrap<CepsConfig> configuration) {
		// nothing to do
	}


	@Override
	@Context
	public void run(CepsConfig configuration, Environment environment) {
		Injector injector = Guice.createInjector(
				new ConfigModule(configuration),
				new DbModule(configuration.getCouchDb()),
				new NotificationsModule(),
				new DataModule(),
				new EventModule(),
				new AdapterModule(),
				new EsperModule(),
				new OdsModule(),
				new RestModule());

		environment.lifecycle().manage(injector.getInstance(NotificationManager.class));
		environment.lifecycle().manage(injector.getInstance(DataManager.class));
		environment.lifecycle().manage(injector.getInstance(EventGarbageCollector.class));
		environment.lifecycle().manage(injector.getInstance(ClientGarbageCollectorManager.class));

		environment.jersey().register(injector.getInstance(DataApi.class));
		environment.jersey().register(injector.getInstance(EventApi.class));
		environment.jersey().register(injector.getInstance(RegistrationApi.class));
		environment.jersey().register(injector.getInstance(SourcesApi.class));
		environment.jersey().register(injector.getInstance(EplAdapterApi.class));
		environment.jersey().register(new DbExceptionMapper());
		environment.jersey().register(new JsonExceptionMapper());
	}



	/*
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
	*/


	/*
	private static void startGarbageCollection() {
	TODO
		// try removing old clients every 3 days (exact removal interval depends on client type)
		NotificationManager notificationManager = NotificationManager.getInstance();
		org.jvalue.ceps.notifications.garbage.GarbageCollector gcmCollector = new GcmGarbageCollector(new GcmUtils("/googleApi.key"));
		GarbageCollectorMapper mapper = new GarbageCollectorMapper(gcmCollector);
		new GarbageCollectorManager(notificationManager, mapper, 259200000).startCollection();
	}
	*/

}
