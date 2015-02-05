package org.jvalue.ceps.adapter;


import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class AdapterModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(EplAdapterManager.class).in(Singleton.class);
	}

}
