package org.jvalue.ceps.adapter;


import com.google.inject.Inject;
import com.google.inject.name.Named;

import java.util.HashMap;
import java.util.Map;

public final class AdapterManager {

	private final Map<String, EplAdapter> adapter = new HashMap<>();


	// static configuration for now
	@Inject
	AdapterManager(@Named(AdapterModule.EPL_ADAPTER_PEGELONLINE) EplAdapter pegelonlineAdapter) {
		adapter.put(pegelonlineAdapter.getName(), pegelonlineAdapter);
	}


	public Map<String, EplAdapter> getAll() {
		return adapter;
	}

}
