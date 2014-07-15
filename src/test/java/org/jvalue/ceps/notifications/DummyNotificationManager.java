package org.jvalue.ceps.notifications;

import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Constructor;
import java.util.Map;

import org.jvalue.ceps.db.DummyDbAccessor;
import org.jvalue.ceps.db.JsonObjectDb;
import org.jvalue.ceps.esper.EsperManager;
import org.jvalue.ceps.event.EventManager;
import org.jvalue.ceps.notifications.clients.Client;
import org.jvalue.ceps.notifications.sender.NotificationSender;


public final class DummyNotificationManager {

	private DummyNotificationManager() { }

	public static NotificationManager createInstance(
			EsperManager esperManager,
			EventManager eventManager,
			Map<Class<?>, NotificationSender<?>> sender) throws Exception {

		assertNotNull(sender);

		Constructor<NotificationManager> constructor 
			= NotificationManager.class.getDeclaredConstructor(
					EsperManager.class,
					EventManager.class,
					Map.class,
					JsonObjectDb.class);
		constructor.setAccessible(true);

		return constructor.newInstance(
				esperManager,
				eventManager,
				sender,
				new JsonObjectDb<Client>(
					new DummyDbAccessor(), 
					Client.class));
	}

}
