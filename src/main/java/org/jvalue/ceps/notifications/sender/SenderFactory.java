package org.jvalue.ceps.notifications.sender;

import org.jvalue.ceps.notifications.clients.GcmClient;


public final class SenderFactory {

	private SenderFactory() { }


	private static NotificationSender<GcmClient> gcmSender;

	public static NotificationSender<GcmClient> getGcmSender() {
		if (gcmSender == null) gcmSender = new GcmSender("/googleApi.key");
		return gcmSender;
	}

}
