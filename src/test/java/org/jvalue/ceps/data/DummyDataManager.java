package org.jvalue.ceps.data;

import java.lang.reflect.Constructor;

import org.jvalue.ceps.db.DummyDbAccessor;


public final class DummyDataManager {

	private DummyDataManager() { }

	public static DataManager createInstance() throws Exception {
		Constructor<DataManager> constructor = DataManager.class.getDeclaredConstructor(
				DataSourceDb.class);
		constructor.setAccessible(true);
		return constructor.newInstance(new DataSourceDb(new DummyDbAccessor())); 
	}

}
