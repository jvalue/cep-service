package org.jvalue.ceps.notifications.sender;


import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;

import org.jvalue.ceps.api.notifications.GcmClient;
import org.jvalue.ceps.api.notifications.HttpClient;


public class SenderModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(new TypeLiteral<NotificationSender<GcmClient>>() { }).to(GcmSender.class);
		bind(new TypeLiteral<NotificationSender<HttpClient>>() { }).to(HttpSender.class);
	}

}
