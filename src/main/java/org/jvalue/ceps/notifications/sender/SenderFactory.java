package org.jvalue.ceps.notifications.sender;

import org.jvalue.ceps.notifications.clients.GcmClient;
import org.jvalue.ceps.notifications.utils.GcmUtils;


public final class SenderFactory {

	private SenderFactory() { }


	private static NotificationSender<GcmClient> gcmSender;

	public static NotificationSender<GcmClient> getGcmSender() {
		if (gcmSender == null) gcmSender = new GcmSender(new GcmUtils("/googleApi.key"));
		return gcmSender;
	}

}
