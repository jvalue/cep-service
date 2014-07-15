package org.jvalue.ceps.event;

import java.lang.reflect.Constructor;

import org.jvalue.ceps.db.DummyDbAccessor;
import org.jvalue.ceps.db.JsonObjectDb;


public final class DummyEventManager {

	private DummyEventManager() { }

	public static EventManager createInstance() throws Exception {
		Constructor<EventManager> constructor = EventManager.class.getDeclaredConstructor(
				JsonObjectDb.class);
		constructor.setAccessible(true);
		return constructor.newInstance(new JsonObjectDb<Event>(
					new DummyDbAccessor(), 
					Event.class));
	}

}
