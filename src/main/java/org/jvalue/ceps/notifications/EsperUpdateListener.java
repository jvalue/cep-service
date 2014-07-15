package org.jvalue.ceps.notifications;

import java.util.List;

import org.jvalue.ceps.esper.JsonUpdateListener;
import org.jvalue.ceps.event.EventManager;
import org.jvalue.ceps.notifications.clients.Client;
import org.jvalue.ceps.notifications.sender.NotificationSender;
import org.jvalue.ceps.notifications.sender.SenderResult;
import org.jvalue.ceps.utils.Assert;
import org.jvalue.ceps.utils.Log;

import com.fasterxml.jackson.databind.JsonNode;


class EsperUpdateListener<C extends Client> implements JsonUpdateListener {

	private final NotificationManager notificationManager;
	private final EventManager eventManager;
	private final C client;
	private final NotificationSender<C> sender;


	public EsperUpdateListener(
			NotificationManager notificationManager, 
			EventManager eventManager, 
			C client, 
			NotificationSender<C> sender) {

		Assert.assertNotNull(notificationManager, eventManager, client, sender);
		this.notificationManager = notificationManager;
		this.eventManager = eventManager;
		this.client = client;
		this.sender = sender;
	}


	public void onNewEvents(List<JsonNode> newEvents, List<JsonNode> oldEvents) {
		String eventId = eventManager.onNewEvents(newEvents, oldEvents);
		SenderResult result = sender.sendEventUpdate(client, eventId, newEvents, oldEvents);

		switch (result.getStatus()) {
			case SUCCESS:
				break;

			case ERROR:
				Log.error("Failed to send notification to client " + client.getClientId());
				if (result.getErrorCause() != null) Log.error("cause", result.getErrorCause() );
				else Log.error(result.getErrorMsg());
				break;

			case REMOVE_CLIENT:
				Log.info("Removing client " + client.getClientId());
				notificationManager.unregister(result.getOldClient().getClientId());
				break;

			case UPDATE_CLIENT:
				Log.info("Updating client " + client.getClientId());
				notificationManager.unregister(result.getOldClient().getClientId());
				notificationManager.register(result.getNewClient());
				break;
		}
	}

}
