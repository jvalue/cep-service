package org.jvalue.ceps.rest;

import org.junit.Test;
import org.jvalue.ceps.rest.DefaultRestlet;
import org.restlet.data.Method;
import org.restlet.data.Status;


public final class DefaultRestletTest {

	@Test
	public final void testGet() {
		test(Method.GET, Status.CLIENT_ERROR_NOT_FOUND);
	}


	@Test
	public final void testPost() {
		test(Method.POST, Status.CLIENT_ERROR_BAD_REQUEST);
	}


	private final void test(Method method, Status status) {
		RestletTestHelper helper = new RestletTestHelper(new DefaultRestlet());
		helper.assertStatus(
				helper.createRequestNoParams(method),
				status);
	}

}
