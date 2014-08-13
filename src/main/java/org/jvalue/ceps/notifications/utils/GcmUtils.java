package org.jvalue.ceps.notifications.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jvalue.ceps.utils.Assert;
import org.jvalue.ceps.utils.Log;

import com.google.android.gcm.server.Constants;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;


public final class GcmUtils {

	private final Sender sender;

	public GcmUtils(String apiKeyResource) {
		Assert.assertNotNull(apiKeyResource);
		String apiKey = getApiKey(apiKeyResource);
		if (apiKey == null) sender = null;
		else sender = new Sender(apiKey);
	}


	public GcmResult sendMsg(String gcmId, Map<String, String> payload) {
		if (sender == null) return new GcmResult.Builder().errorMsg("api key not set").build();

		final List<String> devices = new ArrayList<String>();
		devices.add(gcmId);

		// send
		Message.Builder builder = new Message.Builder();
		for (Map.Entry<String, String> e : payload.entrySet()) {
			builder.addData(e.getKey(), e.getValue());
		}

		MulticastResult multicastResult;
		try {
			multicastResult = sender.send(builder.build(), devices, 5);
		} catch (IOException io) {
			return new GcmResult.Builder().exception(io).build();
		}

		// analyze the results
		List<Result> results = multicastResult.getResults();
		for (int i = 0; i < devices.size(); i++) {
			String regId = devices.get(i);
			Result result = results.get(i);
			String messageId = result.getMessageId();
			if (messageId != null) {
				Log.info("Succesfully sent message to device: " 
					+ regId + "; messageId = " + messageId);
				String canonicalRegId = result.getCanonicalRegistrationId();
				if (canonicalRegId != null) {
					// same device has more than on registration id: update it
					return new GcmResult.Builder().newGcmId(canonicalRegId).build();
				}
			} else {
				String error = result.getErrorCodeName();
				if (error.equals(Constants.ERROR_NOT_REGISTERED)) {
					// application has been removed from device - unregister it
					return new GcmResult.Builder().notRegistered().build();
				} else {
					return new GcmResult.Builder().errorMsg(error).build();
				}
			}
		}

		return new GcmResult.Builder().build();
	}


	private String getApiKey(String resourceName) {
		URL resourceUrl = getClass().getResource(resourceName);

		String key = null;
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(new File(resourceUrl.toURI())));
			key = reader.readLine();
			if (reader.readLine() != null) Log.warn("ApiKey contains more than one line!");

		} catch(Exception e) {
			throw new IllegalArgumentException(e.getMessage());

		} finally {
			try {
				if (reader != null) reader.close();
			} catch(IOException ioc) {
				Log.error(ioc.getMessage());
			}
		}
		return key;
	}


	public static final class GcmResult {

		private final boolean success;
		private final String errorMsg;
		private final boolean notRegistered;
		private final String newGcmId;
		private final IOException exception;

		private GcmResult(
				boolean success, 
				String errorMsg, 
				boolean notRegistered, 
				String newGcmId, 
				IOException exception) {

			this.success = success;
			this.errorMsg = errorMsg;
			this.notRegistered = notRegistered;
			this.newGcmId = newGcmId;
			this.exception = exception;
		}

		public boolean isSuccess() { return success; }
		public String getErrorMsg() { return errorMsg; }
		public boolean isNotRegistered() { return notRegistered; }
		public String getNewGcmId() { return newGcmId; }
		public IOException getException() { return exception; }


		private static class Builder {
			private String errorMsg;
			private boolean notRegistered;
			private String newGcmId;
			private IOException exception;

			public Builder errorMsg(String errorMsg) {
				this.errorMsg = errorMsg;
				return this;
			}

			public Builder notRegistered() {
				this.notRegistered = true;
				return this;
			}

			public Builder newGcmId(String newGcmId) {
				this.newGcmId = newGcmId;
				return this;
			}

			public Builder exception(IOException exception) {
				this.exception = exception;
				return this;
			}

			public GcmResult build() {
				boolean success = (errorMsg == null) && (!notRegistered) && (newGcmId == null) && (exception == null);
				return new GcmResult(success, errorMsg, notRegistered, newGcmId, exception);
			}

		}
	}
}


