package org.jvalue.ceps.ods;


import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import retrofit.RetrofitError;

public final class OdsDataSourceServiceTest extends AbstractOdsServiceTest {

	private static final String
			SOURCE_ID = OdsDataSourceServiceTest.class.getSimpleName(),
			DOMAIN_ID_KEY = "/someDomainIdKey";

	private static final ObjectNode SCHEMA = new ObjectNode(JsonNodeFactory.instance);
	static {
		SCHEMA.put("someKey", "someValue");
	}
	private static final OdsDataSourceMetaData META_DATA = new OdsDataSourceMetaData("", "", "", "", "", "", "");

	private OdsDataSourceService sourceService;

	@Before
	public void setupService() {
		this.sourceService = createService(OdsDataSourceService.class);
	}


	@Test
	public void testSourceCrud() {
		// create
		OdsDataSourceDescription description = new OdsDataSourceDescription(SCHEMA, DOMAIN_ID_KEY, META_DATA);
		OdsDataSource source = sourceService.add(SOURCE_ID, description);
		assertEquals(source);

		// get
		source = sourceService.get(SOURCE_ID);
		assertEquals(source);

		// delete
		sourceService.remove(SOURCE_ID);
		try {
			sourceService.get(SOURCE_ID);
		} catch (RetrofitError re) {
			Assert.assertEquals(404, re.getResponse().getStatus());
			return;
		}

		Assert.fail("source was not removed");
	}


	private void assertEquals(OdsDataSource source) {
		Assert.assertEquals(SOURCE_ID, source.getId());
		Assert.assertEquals(SCHEMA, source.getSchema());
		Assert.assertEquals(DOMAIN_ID_KEY, source.getDomainIdKey());
	}

}
