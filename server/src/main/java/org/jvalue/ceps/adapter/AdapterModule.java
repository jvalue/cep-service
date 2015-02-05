package org.jvalue.ceps.adapter;


import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class AdapterModule extends AbstractModule {

	public static final String EPL_ADAPTER_PEGELONLINE = "eplAdapterPegelOnline";

	@Override
	protected void configure() {
		// bind(EplAdapter.class).annotatedWith(Names.named(EPL_ADAPTER_PEGELONLINE)).to(PegelOnlineEplAdapter.class);
		bind(EplAdapterManager.class).in(Singleton.class);
	}

}
