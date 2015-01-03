package org.jvalue.ceps.ods;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Before;

import retrofit.RestAdapter;
import retrofit.converter.JacksonConverter;

public abstract class AbstractOdsServiceTest {

	private static final String ODS_BASE_URL = "http://localhost:8080/ods/api/v1";

	protected final String
			SOURCE_ID = getClass().getSimpleName(),
			DOMAIN_ID_KEY = "/someDomainIdKey";

	protected final ObjectNode SCHEMA = new ObjectNode(JsonNodeFactory.instance);
	{
		SCHEMA.put("someKey", "someValue");
	}
	protected final OdsDataSourceMetaData META_DATA = new OdsDataSourceMetaData("", "", "", "", "", "", "");

	private final RestAdapter restAdapter;


	protected OdsDataSourceService sourceService;


	public AbstractOdsServiceTest() {
		this.restAdapter = new RestAdapter.Builder()
				.setConverter(new JacksonConverter(new ObjectMapper()))
				.setEndpoint(ODS_BASE_URL)
				.build();
	}


	@Before
	public void setupService() {
		this.sourceService = createService(OdsDataSourceService.class);
	}


	protected OdsDataSource addSource() {
		OdsDataSourceDescription description = new OdsDataSourceDescription(SCHEMA, DOMAIN_ID_KEY, META_DATA);
		return sourceService.add(SOURCE_ID, description);
	}


	protected void removeSource() {
		sourceService.remove(SOURCE_ID);
	}


	protected <T> T createService(Class<T> serviceClass) {
		return restAdapter.create(serviceClass);
	}

}
