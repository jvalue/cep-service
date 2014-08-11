package org.jvalue.ceps.main;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.restlet.routing.Router;


public final class CepApplicationTest {

	@Test
	public final void testInboundRoot() {
		Router root = (Router) new CepApplication().createInboundRoot();

		assertNotNull(root.getDefaultRoute());
		assertEquals(8, root.getRoutes().size());

	}

}
