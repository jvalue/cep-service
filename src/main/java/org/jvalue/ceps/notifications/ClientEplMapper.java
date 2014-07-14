package org.jvalue.ceps.notifications;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jvalue.ceps.esper.EsperManager;
import org.jvalue.ceps.esper.JsonUpdateListener;
import org.jvalue.ceps.notifications.clients.Client;
import org.jvalue.ceps.notifications.sender.NotificationSender;
import org.jvalue.ceps.utils.Assert;

import com.fasterxml.jackson.databind.JsonNode;


final class ClientEplMapper {

	private ClientEplMapper() { }

	private final static Map<String, String> clientToStmtMap = new HashMap<String, String>();

	public static <C extends Client> void register(
			C client,
			NotificationSender<C> sender) {

		Assert.assertNotNull(client, sender);

		EsperUpdateListener<?> esperListener = new EsperUpdateListener<C>(client, sender);
		String stmtId = EsperManager.getInstance().register(client.getEplStmt(), esperListener);

		clientToStmtMap.put(client.getClientId(), stmtId);
	}


	public static void unregister(String clientId) {
		Assert.assertNotNull(clientId);
		Assert.assertTrue(clientToStmtMap.containsKey(clientId), "not registered");
		EsperManager.getInstance().unregister(clientToStmtMap.get(clientId));
	}


	private static class EsperUpdateListener<C extends Client> implements JsonUpdateListener {

		private final C client;
		private final NotificationSender<C> sender;

		public EsperUpdateListener(C client, NotificationSender<C> sender) {
			Assert.assertNotNull(client, sender);
			this.client = client;
			this.sender = sender;
		}


		public void onNewEvents(List<JsonNode> newEvents, List<JsonNode> oldEvents) {
			sender.sendEventUpdate(client, newEvents, oldEvents);
		}

	}

}
