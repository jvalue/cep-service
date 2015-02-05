package org.jvalue.ceps.notifications.garbage;


import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;

import org.jvalue.ceps.notifications.clients.ClientVisitor;
import org.jvalue.ceps.notifications.clients.GcmClient;
import org.jvalue.ceps.notifications.clients.HttpClient;

public class GarbageModule extends AbstractModule {

	public static final String GCM_GARBAGE_COLLECTOR_PERIOD = "gcmGarbageCollectorPeriod";

	@Override
	protected void configure() {
		bind(new TypeLiteral<ClientGarbageCollector<GcmClient>>() { }).to(GcmClientGarbageCollector.class);
		bind(new TypeLiteral<ClientGarbageCollector<HttpClient>>() { }).to(HttpClientGarbageCollector.class);
		bind(new TypeLiteral<ClientVisitor<Void, CollectionStatus>>() { }).to(ClientGarbageCollectorMapper.class);

		bind(ClientGarbageCollectorManager.class).in(Singleton.class);
	}

}
