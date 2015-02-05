package org.jvalue.ceps.utils;

import org.junit.Test;


public final class LogTest {

	@Test
	public void testLog() {
		String msg = "dummy";
		Throwable cause = new RuntimeException("dummy");

		Log.debug(msg);
		Log.debug(msg, cause);
		Log.info(msg);
		Log.info(msg,  cause);
		Log.warn(msg);
		Log.warn(msg, cause);
		Log.error(msg);
		Log.error(msg, cause);
	}

}
