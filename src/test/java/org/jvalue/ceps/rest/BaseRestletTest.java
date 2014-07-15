package org.jvalue.ceps.rest;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.jvalue.ceps.rest.BaseRestlet;
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
		RestletTestHelper helper = new RestletTestHelper(
				new DummyRestlet(
					new HashSet<String>(Arrays.asList("dummy")),
					new	HashSet<String>()),
				new HashSet<String>(Arrays.asList("dummy")),
				new	HashSet<String>());

		helper.assertMissingParams(Method.POST);
		helper.assertMissingParams(Method.GET); 
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
		RestletTestHelper helper = new RestletTestHelper(new DummyRestlet());
		helper.assertInvalidMethod(Method.DELETE);
		helper.assertInvalidMethod(Method.PUT);
	}


	private int 
		doGetCount = 0,
		doPostCount = 0;

	@Test
	public final void testPostAndGet() {
		RestletTestHelper helper = new RestletTestHelper(
				new BaseRestlet(
					new HashSet<String>(Arrays.asList("dummy")),
					new HashSet<String>()) { 

						@Override
						public void doGet(Request request, Response response) {
							doGetCount++;
							onSuccess(response);

							assertEquals("dummy", getParameter(request, "dummy"));
						}

						@Override
						public void doPost(Request request, Response response) {
							doPostCount++;
							onSuccess(response);
						}

					},
				new HashSet<String>(Arrays.asList("dummy")),
				new HashSet<String>());


		// test get
		helper.assertValidMethod(Method.GET);
		assertEquals(1, doGetCount);
		assertEquals(0, doPostCount);

		// test post
		helper.assertValidMethod(Method.POST);
		assertEquals(1, doGetCount);
		assertEquals(1, doPostCount);

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
