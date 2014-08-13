package org.jvalue.ceps.notifications.garbage;

import java.util.HashMap;
import java.util.Map;

import org.jvalue.ceps.notifications.utils.GcmUtils;
import org.jvalue.ceps.utils.Assert;


final class GcmGarbageCollector implements GarbageCollector {

	static final String DATA_KEY_PING = "ping";


	private final GcmUtils gcmUtils;

	public GcmGarbageCollector(GcmUtils gcmUtils) {
		Assert.assertNotNull(gcmUtils);
		this.gcmUtils = gcmUtils;
	}


	@Override
	public CollectionStatus determineStatus(String deviceId) {
		// ping device to check for liveliness
		Map<String, String> payload = new HashMap<String, String>();
		payload.put(DATA_KEY_PING, Boolean.TRUE.toString());

		GcmUtils.GcmResult result = gcmUtils.sendMsg(deviceId, payload);

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
