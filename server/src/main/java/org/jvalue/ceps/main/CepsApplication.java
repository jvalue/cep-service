package org.jvalue.ceps.main;

import com.google.inject.Guice;
import com.google.inject.Injector;

import org.jvalue.ceps.adapter.AdapterModule;
import org.jvalue.ceps.auth.AuthModule;
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
import org.jvalue.ceps.rest.VersionApi;
import org.jvalue.commons.auth.AuthBinder;
import org.jvalue.commons.auth.BasicAuthUserDescription;
import org.jvalue.commons.auth.UserManager;
import org.jvalue.commons.auth.rest.UnauthorizedExceptionMapper;
import org.jvalue.commons.auth.rest.UserApi;
import org.jvalue.commons.couchdb.rest.DbExceptionMapper;
import org.jvalue.commons.rest.JsonExceptionMapper;

import java.util.List;

import javax.ws.rs.core.Context;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;


public final class CepsApplication extends Application<CepsConfig> {

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
				new RestModule(),
				new AuthModule(configuration.getAuth()));

		setupDefaultUsers(injector.getInstance(UserManager.class), configuration.getAuth().getUsers());

		environment.lifecycle().manage(injector.getInstance(DataManager.class));
		environment.lifecycle().manage(injector.getInstance(NotificationManager.class));
		environment.lifecycle().manage(injector.getInstance(EventGarbageCollector.class));
		environment.lifecycle().manage(injector.getInstance(ClientGarbageCollectorManager.class));

		environment.jersey().getResourceConfig().register(injector.getInstance(AuthBinder.class));
		environment.jersey().register(injector.getInstance(DataApi.class));
		environment.jersey().register(injector.getInstance(EventApi.class));
		environment.jersey().register(injector.getInstance(RegistrationApi.class));
		environment.jersey().register(injector.getInstance(SourcesApi.class));
		environment.jersey().register(injector.getInstance(EplAdapterApi.class));
		environment.jersey().register(injector.getInstance(VersionApi.class));
		environment.jersey().register(injector.getInstance(UserApi.class));
		environment.jersey().register(new DbExceptionMapper());
		environment.jersey().register(new JsonExceptionMapper());
		environment.jersey().register(new UnauthorizedExceptionMapper());
	}


	private void setupDefaultUsers(UserManager userManager, List<BasicAuthUserDescription> userList) {
		for (BasicAuthUserDescription user : userList) {
			if (!userManager.contains(user.getEmail())) userManager.add(user);
		}
	}

}
