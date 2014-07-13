package org.jvalue.ceps.rest;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Method;
import org.restlet.data.Reference;
import org.restlet.data.Status;


public final class DefaultRestletTest {

	@Test
	public final void testGet() {
		test(Method.GET);
	}


	@Test
	public final void testPost() {
		test(Method.POST);
	}


	private final void test(Method method) {
		DefaultRestlet restlet = new DefaultRestlet();

		Request request = new Request(method, new Reference());
		Response response = new Response(request);
		restlet.handle(request, response);

		assertEquals(Status.CLIENT_ERROR_NOT_FOUND, response.getStatus());
	}

}
