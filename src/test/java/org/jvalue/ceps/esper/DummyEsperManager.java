package org.jvalue.ceps.esper;

import java.lang.reflect.Constructor;


public final class DummyEsperManager {

	private DummyEsperManager() { }

	public static EsperManager createInstance(String engineName) throws Exception {
		Constructor<EsperManager> constructor = EsperManager.class.getDeclaredConstructor(
				String.class);
		constructor.setAccessible(true);
		return constructor.newInstance(engineName);
	}

}
