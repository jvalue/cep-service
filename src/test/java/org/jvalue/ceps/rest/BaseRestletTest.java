package org.jvalue.ceps.rest;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Method;
import org.restlet.data.Reference;
import org.restlet.data.Status;


public final class BaseRestletTest {

	@Test(expected = NullPointerException.class)
	public final void testNullParams() {
		new DummyRestlet(null, null);
	}


	@Test(expected = IllegalArgumentException.class)
	public final void testOverlappingParams() {
		new DummyRestlet(
				new HashSet<String>(Arrays.asList("dummy")),
				new HashSet<String>(Arrays.asList("dummy")));
	}


	@Test
	public final void testNoParams() {
		new DummyRestlet();
	}


	@Test
	public final void testMissingParamRequest() {
		BaseRestlet restlet = new DummyRestlet(
				new HashSet<String>(Arrays.asList("dummy")),
				new	HashSet<String>());

		Request request = new Request(Method.POST, new Reference());
		Response response = new Response(request);

		restlet.handle(request, response);

		assertEquals(Status.CLIENT_ERROR_BAD_REQUEST, response.getStatus());
	}


	@Test
	public final void testAdditionalParamRequest() {
		BaseRestlet restlet = new DummyRestlet(
				new HashSet<String>(Arrays.asList("dummy")),
				new	HashSet<String>());

		Request request = new Request(Method.POST, new Reference());
		request.getResourceRef().addQueryParameter("dummy", "dummy");
		request.getResourceRef().addQueryParameter("dummy2", "dummy2");
		Response response = new Response(request);

		restlet.handle(request, response);

		assertEquals(Status.CLIENT_ERROR_BAD_REQUEST, response.getStatus());
	}


	@Test
	public final void testInvalidMethod() {
		BaseRestlet restlet = new DummyRestlet();

		Request request = new Request(Method.DELETE, new Reference());
		Response response = new Response(request);

		restlet.handle(request, response);

		assertEquals(Status.CLIENT_ERROR_BAD_REQUEST, response.getStatus());
	}


	private int 
		doGetCount = 0,
		doPostCount = 0;

	@Test
	public final void testPostAndGet() {
		BaseRestlet restlet = new BaseRestlet(
				new HashSet<String>(Arrays.asList("dummy")),
				new HashSet<String>()) {

			@Override
			public void doGet(Request request, Response response) {
				doGetCount++;
				onSuccess(response);

				assertEquals("value", getParameter(request, "dummy"));
			}

			@Override
			public void doPost(Request request, Response response) {
				doPostCount++;
				onSuccess(response);
			}

		};

		// test get
		Request getRequest = new Request(Method.GET, new Reference());
		getRequest.getResourceRef().addQueryParameter("dummy", "value");
		Response getResponse = new Response(getRequest);

		restlet.handle(getRequest, getResponse);

		assertEquals(1, doGetCount);
		assertEquals(0, doPostCount);
		assertEquals(Status.SUCCESS_OK, getResponse.getStatus());

		// test post
		Request postRequest = new Request(Method.POST, new Reference());
		Response postResponse = new Response(postRequest);

		restlet.handle(postRequest, postResponse);

		assertEquals(1, doGetCount);
		assertEquals(1, doPostCount);
		assertEquals(Status.SUCCESS_OK, getResponse.getStatus());

	}


	private static class DummyRestlet  extends BaseRestlet {

		public DummyRestlet (
				Set<String> mandatoryQueryParams,
				Set<String> optionalQueryParams) {

			super(mandatoryQueryParams, optionalQueryParams);
		}

		public DummyRestlet() {
			super(new HashSet<String>(), new HashSet<String>());
		}

		@Override
		public void doGet(Request request, Response response) { } 

		@Override
		public void doPost(Request request, Response response) { }

	}

}
