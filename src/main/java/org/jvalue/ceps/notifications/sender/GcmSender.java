package org.jvalue.ceps.notifications.sender;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jvalue.ceps.notifications.clients.GcmClient;
import org.jvalue.ceps.notifications.utils.GcmUtils;
import org.jvalue.ceps.utils.Assert;

import com.fasterxml.jackson.databind.JsonNode;


final class GcmSender extends NotificationSender<GcmClient> {

	static final String 
		DATA_KEY_CLIENT_ID = "client",
		DATA_KEY_EVENT_ID = "event",
		DATA_KEY_DEBUG = "debug";


	private final GcmUtils gcmUtils;

	GcmSender(GcmUtils gcmUtils) {
		Assert.assertNotNull(gcmUtils);
		this.gcmUtils = gcmUtils;
	}

	@Override
	public SenderResult sendEventUpdate(
			GcmClient client, 
			String eventId,
			List<JsonNode> newEvents, 
			List<JsonNode> oldEvents) {

		// gather data
		Map<String,String> payload = new HashMap<String,String>();
		payload.put(DATA_KEY_CLIENT_ID, client.getClientId());
		payload.put(DATA_KEY_EVENT_ID, eventId);
		payload.put(DATA_KEY_DEBUG, Boolean.TRUE.toString());

		GcmUtils.GcmResult result = gcmUtils.sendMsg(client.getDeviceId(), payload);

		if (result.isSuccess()) return getSuccessResult();
		else if (result.getErrorMsg() != null) return getErrorResult(result.getErrorMsg());
		else if (result.getException() != null) return getErrorResult(result.getException());
		else if (result.getNewGcmId() != null) return getUpdateClientResult(client.getDeviceId(), result.getNewGcmId());
		else if (result.isNotRegistered()) return getRemoveClientResult(client.getDeviceId());

		throw new IllegalStateException("ups, this shouldn't have happened ...");
	}

}
