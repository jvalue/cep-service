package org.jvalue.ceps.rest;


import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

public class RestModule extends AbstractModule {

	public static final String URL_DATA = "urlData";

	@Override
	protected void configure() {
		bind(String.class).annotatedWith(Names.named(URL_DATA)).toInstance(DataApi.URL_DATA);
	}


}
