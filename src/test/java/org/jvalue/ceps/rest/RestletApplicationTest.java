package org.jvalue.ceps.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.restlet.routing.Router;


public final class RestletApplicationTest {

	@Test
	public final void testInboundRoot() {
		Router root = (Router) new RestletApplication().createInboundRoot();

		assertNotNull(root.getDefaultRoute());
		assertEquals(4, root.getRoutes().size());

	}

}
