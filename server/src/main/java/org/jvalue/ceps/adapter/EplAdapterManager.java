package org.jvalue.ceps.adapter;


import com.google.inject.Inject;

import org.jvalue.ceps.api.adapter.EplAdapter;

import java.util.HashMap;
import java.util.Map;

public final class EplAdapterManager {

	private final Map<String, EplAdapter> adapter = new HashMap<>();


	// static configuration for now
	@Inject
	EplAdapterManager() {
		// EplAdapterManager(@Named(AdapterModule.EPL_ADAPTER_PEGELONLINE) EplAdapter pegelonlineAdapter) {
		// adapter.put(pegelonlineAdapter.getId(), pegelonlineAdapter);
	}


	public EplAdapter getByName(String name) {
		return adapter.get(name);
	}

}
