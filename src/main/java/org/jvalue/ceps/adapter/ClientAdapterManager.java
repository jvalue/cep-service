package org.jvalue.ceps.adapter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public final class ClientAdapterManager {

	private static final Map<String, ClientAdapter> adapters;
	static {
		Map<String, ClientAdapter> tmpAdapters = new HashMap<String, ClientAdapter>();
		tmpAdapters.put("/de/pegelonline/levelAlarm", new PegelOnlineAdapter());
		adapters = Collections.unmodifiableMap(tmpAdapters);
	}


	public Map<String, ClientAdapter> getAdapters() {
		return adapters;
	}

}
