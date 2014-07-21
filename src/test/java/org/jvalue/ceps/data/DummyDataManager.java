package org.jvalue.ceps.data;

import java.lang.reflect.Constructor;

import org.jvalue.ceps.db.DummyDbAccessor;
import org.jvalue.ceps.db.JsonObjectDb;


public final class DummyDataManager {

	private DummyDataManager() { }

	public static DataManager createInstance() throws Exception {
		Constructor<DataManager> constructor = DataManager.class.getDeclaredConstructor(
				JsonObjectDb.class);
		constructor.setAccessible(true);
		return constructor.newInstance(new JsonObjectDb<DataSourceRegistration>(
					new DummyDbAccessor(), 
					DataSourceRegistration.class));
	}

}
