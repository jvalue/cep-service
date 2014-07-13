package org.jvalue.ceps.rest;

import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;
import org.restlet.Restlet;
import org.restlet.data.Method;


public final class DataRestApiTest {

	@Test
	public final void testNewDataRestlet() {
		Restlet restlet = new DataRestApi().getRoutes().get(OdsRestHook.URL_NOTIFY_SOURCE_CHANGED);
		assertNotNull(restlet);

		RestletTestHelper helper = new RestletTestHelper(
				restlet, 
				new HashSet<String>(Arrays.asList(OdsRestHook.PARAM_SOURCE)), 
				new HashSet<String>());
		helper.assertInvalidMethod(Method.GET);
		helper.assertMissingParams(Method.POST);
	}

}
