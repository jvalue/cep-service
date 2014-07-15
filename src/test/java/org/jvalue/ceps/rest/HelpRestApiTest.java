package org.jvalue.ceps.rest;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.jvalue.ceps.rest.restlet.RestletTestHelper;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.Method;


public final class HelpRestApiTest {

	@Test
	public final void testHelpRestlet() {
		List<String> helpContent = Arrays.asList("help", "dummy", "foobar");
		Restlet restlet = new HelpRestApi(helpContent).getRoutes().get("/api");
		assertNotNull(restlet);

		RestletTestHelper helper = new RestletTestHelper(restlet);
		helper.assertInvalidMethod(Method.POST);
		Response response = helper.assertValidMethod(Method.GET);

		String helpText = response.getEntityAsText();
		for (String content : helpContent) {
			assertTrue(helpText.contains(content));
		}
	}

}
