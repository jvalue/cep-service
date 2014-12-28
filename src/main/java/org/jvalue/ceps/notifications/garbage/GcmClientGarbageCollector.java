package org.jvalue.ceps.notifications.garbage;

import com.google.inject.Inject;

import org.jvalue.ceps.notifications.clients.GcmClient;
import org.jvalue.ceps.notifications.utils.GcmUtils;
import org.jvalue.ceps.utils.Assert;

import java.util.HashMap;
import java.util.Map;


final class GcmClientGarbageCollector implements ClientGarbageCollector<GcmClient> {

	static final String DATA_KEY_PING = "ping";


	private final GcmUtils gcmUtils;

	@Inject
	GcmClientGarbageCollector(GcmUtils gcmUtils) {
		Assert.assertNotNull(gcmUtils);
		this.gcmUtils = gcmUtils;
	}


	@Override
	public CollectionStatus determineStatus(GcmClient client) {
		// ping device to check for liveliness
		Map<String, String> payload = new HashMap<String, String>();
		payload.put(DATA_KEY_PING, Boolean.TRUE.toString());

		GcmUtils.GcmResult result = gcmUtils.sendMsg(client.getDeviceId(), payload);

		// Only remove when google says so.
		// This ignores all kind of errors, but ensures that no valid client is removed
		// on accident --> rather have garbage clients than remove vlaid ones
		if (result.isNotRegistered()) {
			return CollectionStatus.COLLECT;
		} else {
			return CollectionStatus.RETAIN;
		} 
	}

}
