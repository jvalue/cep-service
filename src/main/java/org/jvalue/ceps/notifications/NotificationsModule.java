package org.jvalue.ceps.notifications;


import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;

import org.jvalue.ceps.notifications.clients.ClientVisitor;
import org.jvalue.ceps.notifications.garbage.CollectionStatus;
import org.jvalue.ceps.notifications.garbage.GarbageCollectorManager;
import org.jvalue.ceps.notifications.garbage.GarbageCollectorMapper;
import org.jvalue.ceps.notifications.sender.SenderModule;

public class NotificationsModule extends AbstractModule {

	public static final String
			GCM_API_KEY = "gcmApiKey",
			GCM_GARBAGE_COLLECTOR_PERIOD = "gcmGarbageCollectorPeriod";

	@Override
	protected void configure() {
		install(new SenderModule());
		bind(NotificationManager.class).in(Singleton.class);
		bind(GarbageCollectorManager.class).in(Singleton.class);
		bind(new TypeLiteral<ClientVisitor<Void, CollectionStatus>>() { }).to(GarbageCollectorMapper.class);
	}

}
