package org.jvalue.ceps.notifications.sender;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jvalue.ceps.notifications.clients.ClientFactory;
import org.jvalue.ceps.notifications.clients.GcmClient;
import org.jvalue.ceps.utils.Assert;
import org.jvalue.ceps.utils.Log;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.android.gcm.server.Constants;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;


final class GcmSender extends NotificationSender<GcmClient> {

	static final String 
		DATA_KEY_CLIENT_ID = "client",
		DATA_KEY_EVENT_ID = "event",
		DATA_KEY_DEBUG = "debug";


	private final Sender sender;

	GcmSender(String apiKeyResource) {
		Assert.assertNotNull(apiKeyResource);
		String apiKey = new GcmApiKey(apiKeyResource).toString();
		if (apiKey == null) sender = null;
		else sender = new Sender(apiKey);
	}

	@Override
	public SenderResult sendEventUpdate(
			GcmClient client, 
			String eventId,
			List<JsonNode> newEvents, 
			List<JsonNode> oldEvents) {

		if (sender == null) return getErrorResult("api key not set");

		// gather data
		Map<String,String> payload = new HashMap<String,String>();
		payload.put(DATA_KEY_CLIENT_ID, client.getClientId());
		payload.put(DATA_KEY_EVENT_ID, eventId);
		payload.put(DATA_KEY_DEBUG, Boolean.TRUE.toString());

		final List<String> devices = new ArrayList<String>();
		devices.add(client.getDeviceId());

		// send
		Message.Builder builder = new Message.Builder();
		for (Map.Entry<String, String> e : payload.entrySet()) {
			builder.addData(e.getKey(), e.getValue());
		}

		MulticastResult multicastResult;
		try {
			multicastResult = sender.send(builder.build(), devices, 5);
		} catch (IOException io) {
			return getErrorResult(io);
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
					return getUpdateClientResult(
							client, 
							ClientFactory.createGcmClient(client, canonicalRegId));
				}
			} else {
				String error = result.getErrorCodeName();
				if (error.equals(Constants.ERROR_NOT_REGISTERED)) {
					// application has been removed from device - unregister it
					return getRemoveClientResult(client);
				} else {
					return getErrorResult(error);
				}
			}
		}

		return getSuccessResult();
	}

}
