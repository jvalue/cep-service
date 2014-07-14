package org.jvalue.ceps.notifications;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jvalue.ceps.esper.EsperManager;
import org.jvalue.ceps.esper.JsonUpdateListener;
import org.jvalue.ceps.utils.Assert;

import com.fasterxml.jackson.databind.JsonNode;


final class ClientEplMapper {

	private ClientEplMapper() { }

	private final static Map<String, String> clientToStmtMap = new HashMap<String, String>();

	public static <C extends Client> void register(
			C client,
			ClientEventListener<C> listener) {

		Assert.assertNotNull(client, listener);

		EsperUpdateListener<?> esperListener = new EsperUpdateListener<C>(client, listener);
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
		private final ClientEventListener<C> listener;

		public EsperUpdateListener(C client, ClientEventListener<C> listener) {
			Assert.assertNotNull(client, listener);
			this.client = client;
			this.listener = listener;
		}


		public void onNewEvents(List<JsonNode> newEvents, List<JsonNode> oldEvents) {
			listener.onNewEvents(client, newEvents, oldEvents);
		}

	}

}
