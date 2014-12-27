package org.jvalue.ceps.main;


import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

import org.jvalue.ceps.event.EventModule;

public class ConfigModule extends AbstractModule {

	private final CepsConfig config;

	public ConfigModule(CepsConfig config) {
		this.config = config;
	}


	@Override
	protected void configure() {
		bind(long.class).annotatedWith(Names.named(EventModule.EVENT_GARBAGE_COLLECTOR_PERIOD)).toInstance(config.getEventGarbageCollectionPeriod());
		bind(long.class).annotatedWith(Names.named(EventModule.EVENT_GARBAGE_COLLECTOR_MAX_AGE)).toInstance(config.getEventGarbageCollectionMaxAge());
	}

}
