package org.jvalue.ceps.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.net.HttpURLConnection;

import org.junit.Test;


public final class RestExceptionTest {

	@Test
	public void testCauseError() {
		IOException cause = new IOException("error");
		RestException re2 = new RestException(cause);
		assertEquals(cause, re2.getCause());
		assertNotNull(re2.getMessage());
	}


	@Test
	public void testCodeError() {
		testCode(HttpURLConnection.HTTP_NOT_MODIFIED);
		testCode(HttpURLConnection.HTTP_BAD_REQUEST);
		testCode(HttpURLConnection.HTTP_UNAUTHORIZED);
		testCode(HttpURLConnection.HTTP_FORBIDDEN);
		testCode(HttpURLConnection.HTTP_NOT_FOUND);
		testCode(HttpURLConnection.HTTP_BAD_GATEWAY);
		testCode(HttpURLConnection.HTTP_BAD_METHOD);
		testCode(HttpURLConnection.HTTP_GONE);
		testCode(HttpURLConnection.HTTP_OK);
	}


	private void testCode(int code) {
		RestException re = new RestException(code);
		assertEquals(code, re.getCode());
		assertNotNull(re.getMessage());
	}


}
