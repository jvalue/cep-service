package org.jvalue.ceps.ods;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.BaseEncoding;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import org.jvalue.ceps.main.OdsConfig;
import org.jvalue.ods.api.DataSourceApi;
import org.jvalue.ods.api.NotificationApi;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.converter.JacksonConverter;

public class OdsModule extends AbstractModule {

	@Override
	protected void configure() {
		// nothing to do yet ...
	}


	@Provides
	@Singleton
	DataSourceApi provideDataSourceApi(RestAdapter restAdapter) {
		return restAdapter.create(DataSourceApi.class);
	}


	@Provides
	@Singleton
	NotificationApi provideNotificationApi(RestAdapter restAdapter) {
		return restAdapter.create(NotificationApi.class);
	}


	@Provides
	@Singleton
	RestAdapter provideRestAdapter(final OdsConfig odsConfig) {
		byte[] credentials = (odsConfig.getUsername() + ":" + odsConfig.getPassword()).getBytes();
		final String authHeader = "Basic " + BaseEncoding.base64().encode(credentials);

		return new RestAdapter.Builder()
				.setConverter(new JacksonConverter(new ObjectMapper()))
				.setEndpoint(odsConfig.getUrl())
				.setRequestInterceptor(new RequestInterceptor() {
					@Override
					public void intercept(RequestFacade request) {
						request.addHeader("Authorization", authHeader);
					}
				})
				.build();
	}
}
