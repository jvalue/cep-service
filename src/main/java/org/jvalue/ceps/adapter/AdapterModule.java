package org.jvalue.ceps.adapter;


import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

public class AdapterModule extends AbstractModule {

	public static final String ADAPTER_PEGELONLINE = "adapterPegelOnline";

	@Override
	protected void configure() {
		bind(EplAdapter.class).annotatedWith(Names.named(ADAPTER_PEGELONLINE)).to(PegelOnlineAdapter.class);
	}

}
