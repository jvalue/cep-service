package org.jvalue.ceps.utils;

import java.io.IOException;
import java.net.HttpURLConnection;


public final class RestException extends Exception {
	public static final long serialVersionUID = 42L;

	public static final int UNSET  = -1;
	private final int code;

	public RestException(int code) {
		this.code = code;
	}

	public RestException(IOException nestedException) {
		super(nestedException);
		this.code = RestException.UNSET;
	}

	public int getCode() {
		return code;
	}

	@Override
	public String getMessage() {
		Throwable cause = getCause();
		if (cause != null) return "Failed to communicate with servers (" + cause.getMessage() + ")";
		
		if (code == HttpURLConnection.HTTP_NOT_MODIFIED) {
			return "The requested documents were not modified";
		} else if (code == HttpURLConnection.HTTP_BAD_REQUEST) {
			return "The servers did not understand the request";
		} else if (code == HttpURLConnection.HTTP_UNAUTHORIZED) {
			return "The credentials provided were not correct";
		} else if (code == HttpURLConnection.HTTP_FORBIDDEN) {
			return "The request is not allowed to make these changes";
		} else if (code == HttpURLConnection.HTTP_NOT_FOUND) {
			return "The requested resource is not available";
		} else if (code >= 500 && code <= 600) {
			return "Unable to connect to the servers";
		}
		return "Unknown error (" + code + ")";
	}
}
