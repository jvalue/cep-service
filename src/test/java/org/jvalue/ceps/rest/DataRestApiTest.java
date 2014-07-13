package org.jvalue.ceps.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.Method;
import org.restlet.data.Reference;
import org.restlet.data.Status;


public final class DataRestApiTest {

	@Test
	public final void testGetRoutes() {
		Map<String, Restlet> routes = new DataRestApi().getRoutes();

		assertNotNull(routes);
		assertEquals(1, routes.size());
		assertTrue(routes.keySet().contains(OdsRestHook.URL_NOTIFY_SOURCE_CHANGED));
	}


	@Test
	public final void testNewDataRestlet() {
		Restlet restlet = new DataRestApi().getRoutes().get(OdsRestHook.URL_NOTIFY_SOURCE_CHANGED);

		Request missingParamRequest = new Request(Method.POST, new Reference());
		Response missingParamResponse = new Response(missingParamRequest);
		restlet.handle(missingParamRequest, missingParamResponse);

		assertEquals(Status.CLIENT_ERROR_BAD_REQUEST, missingParamResponse.getStatus());

		Request badMethodRequest = new Request(Method.GET, new Reference());
		badMethodRequest.getResourceRef().addQueryParameter(OdsRestHook.PARAM_SOURCE, "dummy");
		Response badMethodResponse = new Response(badMethodRequest);
		restlet.handle(badMethodRequest, badMethodResponse);

		assertEquals(Status.CLIENT_ERROR_BAD_REQUEST, missingParamResponse.getStatus());

	}

}
