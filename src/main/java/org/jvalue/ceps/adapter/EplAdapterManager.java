package org.jvalue.ceps.adapter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public final class EplAdapterManager {

	private static final Map<String, EplAdapter> adapters;
	static {
		Map<String, EplAdapter> tmpAdapters = new HashMap<String, EplAdapter>();
		tmpAdapters.put("/de/pegelonline/levelAlarm", new PegelOnlineAdapter());
		adapters = Collections.unmodifiableMap(tmpAdapters);
	}


	public Map<String, EplAdapter> getAdapters() {
		return adapters;
	}

}
