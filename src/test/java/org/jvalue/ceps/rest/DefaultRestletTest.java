package org.jvalue.ceps.rest;

import org.junit.Test;
import org.restlet.data.Method;
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
		RestletTestHelper helper = new RestletTestHelper(new DefaultRestlet());
		helper.assertStatus(
				helper.createRequestNoParams(method),
				Status.CLIENT_ERROR_NOT_FOUND);
	}

}
