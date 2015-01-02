package org.jvalue.ceps.ods;


import com.fasterxml.jackson.databind.ObjectMapper;

import retrofit.RestAdapter;
import retrofit.converter.JacksonConverter;

public abstract class AbstractOdsServiceTest {

	private static final String ODS_BASE_URL = "http://localhost:8080/ods/api/v1";

	private final RestAdapter restAdapter;

	public AbstractOdsServiceTest() {
		this.restAdapter = new RestAdapter.Builder()
				.setConverter(new JacksonConverter(new ObjectMapper()))
				.setEndpoint(ODS_BASE_URL)
				.build();
	}


	protected <T> T createService(Class<T> serviceClass) {
		return restAdapter.create(serviceClass);
	}

}
