package org.jvalue.ceps.notifications.sender;


import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;

import org.jvalue.ceps.notifications.clients.GcmClient;

public class SenderModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(new TypeLiteral<NotificationSender<GcmClient>>() { }).to(GcmSender.class);
	}

}
