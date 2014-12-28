package org.jvalue.ceps.notifications;


import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import org.jvalue.ceps.notifications.garbage.GarbageModule;
import org.jvalue.ceps.notifications.sender.SenderModule;

public class NotificationsModule extends AbstractModule {

	public static final String GCM_API_KEY = "gcmApiKey";

	@Override
	protected void configure() {
		install(new SenderModule());
		install(new GarbageModule());

		bind(NotificationManager.class).in(Singleton.class);
	}

}
