package org.jvalue.ceps.event;


import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class EventModule extends AbstractModule {

	public static final String
			EVENT_GARBAGE_COLLECTOR_PERIOD = "eventGarbageCollectorPeriod",
			EVENT_GARBAGE_COLLECTOR_MAX_AGE = "eventGarbageCollectorMaxAge";


	@Override
	protected void configure() {
		bind(EventManager.class).in(Singleton.class);
		bind(EventGarbageCollector.class).in(Singleton.class);
	}

}
