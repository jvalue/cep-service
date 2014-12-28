package org.jvalue.ceps.data;

import java.lang.reflect.Constructor;

import org.jvalue.ceps.db.DummyDbAccessor;
import org.jvalue.ceps.db.JsonObjectDb;
import org.jvalue.ceps.esper.DataUpdateListener;


public final class DummyDataManager {

	private DummyDataManager() { }

	public static DataManager createInstance(DataUpdateListener listener) throws Exception {
		Constructor<DataManager> constructor = DataManager.class.getDeclaredConstructor(
				JsonObjectDb.class,
				DataUpdateListener.class);
		constructor.setAccessible(true);
		return constructor.newInstance(
				new JsonObjectDb<OdsRegistration>(
					new DummyDbAccessor(), 
					OdsRegistration.class),
				listener);
	}

}
