package org.jvalue.ceps.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;


public final class RestCallTest {

	@Test
	public void testSuccess() throws RestException {

		RestCall call = new RestCall.Builder(
				RestCall.RequestType.GET, 
				"http://pegelonline.wsv.de")
			.path("webservices")
			.path("rest-api")
			.path("v2")
			.path("stations.json")
			.parameter("waters", "RHEIN")
			.header("From", "me@home.com")
			.build();

		assertNotNull(call.execute());

	}


	@Test(expected = RestException.class)
	public void testFailure() throws RestException {

		RestCall call = new RestCall.Builder(
				RestCall.RequestType.POST, 
				"http://pegelonline.wsv.de")
			.path("dummy")
			.build();

		call.execute();

	}

	
	@Test
	public void testRequestType() {
		assertEquals("GET", RestCall.RequestType.GET.toString());
		assertEquals("POST", RestCall.RequestType.POST.toString());
		assertEquals("UPDATE", RestCall.RequestType.UPDATE.toString());
		assertEquals("DELETE", RestCall.RequestType.DELETE.toString());
	}


}
