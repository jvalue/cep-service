package org.jvalue.ceps.data;


import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class DataModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(DataManager.class).in(Singleton.class);
	}

}
