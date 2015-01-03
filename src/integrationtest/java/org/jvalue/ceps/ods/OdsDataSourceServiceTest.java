package org.jvalue.ceps.ods;


import org.junit.Assert;
import org.junit.Test;

import retrofit.RetrofitError;

public final class OdsDataSourceServiceTest extends AbstractOdsServiceTest {

	@Test
	public void testSourceCrud() {
		// create
		OdsDataSource source = addSource();
		assertEquals(source);

		// get
		source = sourceService.get(SOURCE_ID);
		assertEquals(source);

		// delete
		removeSource();
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
