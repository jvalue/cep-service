package org.jvalue.ceps.notifications.utils;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;


public final class GcmUtilsTest {

	@Test
	public void testFailSend() {
		try {
			GcmUtils utils = new GcmUtils("someInvalidApiKey");
			GcmUtils.GcmResult result = utils.sendMsg("dummy", new HashMap<String, String>());

			Assert.assertNotNull(result);
			Assert.assertFalse(result.isSuccess());
			Assert.assertTrue(result.getErrorMsg() != null || result.getException() != null);
		} catch (IllegalArgumentException iae) {
			// TODO this seems to be a GCM bug unfortunately, see http://stackoverflow.com/a/17491468
			// short version: GCM throws wrong exception on invalid API key
		}
	}

}
