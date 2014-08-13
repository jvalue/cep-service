package org.jvalue.ceps.notifications.utils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import org.junit.Test;


public final class GcmUtilsTest {

	@Test
	public final void testFailSend() {
		try {
			GcmUtils utils = new GcmUtils(getApiKeyResourceName());
			GcmUtils.GcmResult result = utils.sendMsg("dummy", new HashMap<String, String>());

			assertNotNull(result);
			assertFalse(result.isSuccess());
			assertTrue(result.getErrorMsg() != null || result.getException() != null);

		} catch (IllegalArgumentException iae) {
			// sender does not work when apikey is not set
			assertFalse(isApiKeyPresent());
		}
	}


	private static final String 
		RESOURCE_NAME = "/googleApi.key",
		DUMMY_RESOURCE_NAME = "/googleApi.key.template";


	private String getApiKeyResourceName() {
		if (isApiKeyPresent()) return RESOURCE_NAME;
		else return DUMMY_RESOURCE_NAME;
	}


	private boolean isApiKeyPresent() {
		return GcmUtils.class.getResource(RESOURCE_NAME) != null;
	}

}
