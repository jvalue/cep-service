package org.jvalue.ceps.adapter;


import com.google.inject.Inject;
import com.google.inject.name.Named;

import java.util.HashMap;
import java.util.Map;

public final class EplAdapterManager {

	private final Map<String, EplAdapter> adapter = new HashMap<>();


	// static configuration for now
	@Inject
	EplAdapterManager(@Named(AdapterModule.EPL_ADAPTER_PEGELONLINE) EplAdapter pegelonlineAdapter) {
		adapter.put(pegelonlineAdapter.getName(), pegelonlineAdapter);
	}


	public EplAdapter getByName(String name) {
		return adapter.get(name);
	}

}
