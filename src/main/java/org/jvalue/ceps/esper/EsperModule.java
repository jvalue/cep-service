package org.jvalue.ceps.esper;


import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class EsperModule extends AbstractModule {

	public static final String ESPER_ENGINE_NAME = "esperEngineName";

	@Override
	protected void configure() {
		bind(EsperManager.class).in(Singleton.class);
	}

}
