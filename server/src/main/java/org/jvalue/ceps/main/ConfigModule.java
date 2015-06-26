package org.jvalue.ceps.main;


import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

import org.jvalue.ceps.notifications.NotificationsModule;
import org.jvalue.ceps.notifications.garbage.GarbageModule;

public class ConfigModule extends AbstractModule {

	public static final String BASE_URL = "baseUrl";

	private final CepsConfig config;

	public ConfigModule(CepsConfig config) {
		this.config = config;
	}


	@Override
	protected void configure() {
		bind(String.class).annotatedWith(Names.named(BASE_URL)).toInstance(config.getUrl());
		bind(OdsConfig.class).toInstance(config.getOds());

		bind(String.class).annotatedWith(Names.named(NotificationsModule.GCM_API_KEY)).toInstance(config.getGcmApiKey());
		bind(long.class).annotatedWith(Names.named(GarbageModule.GCM_GARBAGE_COLLECTOR_PERIOD)).toInstance(config.getGcmGarbageCollectorPeriod());
	}

}
