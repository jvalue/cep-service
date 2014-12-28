package org.jvalue.ceps.esper;


import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class EsperModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(EsperManager.class).in(Singleton.class);
		bind(DataUpdateListener.class).to(EsperManager.class);
		bind(EPServiceProvider.class).toInstance(EPServiceProviderManager.getProvider("esper"));
	}

}
