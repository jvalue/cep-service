package org.jvalue.ceps.notifications.sender;

import java.util.List;

import org.jvalue.ceps.notifications.clients.Client;

import com.fasterxml.jackson.databind.JsonNode;


public abstract class NotificationSender<C extends Client> {

	public abstract SenderResult sendEventUpdate(
			C client, 
			String eventId,
			List<JsonNode> newEvents, 
			List<JsonNode> oldEvents);


	protected SenderResult getSuccessResult() {
		return new SenderResult.Builder(SenderResult.Status.SUCCESS).build();
	}


	protected SenderResult getErrorResult(String msg) {
		return new SenderResult.Builder(SenderResult.Status.ERROR).errorMsg(msg).build();
	}


	protected SenderResult getErrorResult(Throwable cause) {
		return new SenderResult.Builder(SenderResult.Status.ERROR).errorCause(cause).build();
	}

	
	protected SenderResult getUpdateClientResult(Client oldClient, Client newClient) {
		return new SenderResult.Builder(SenderResult.Status.UPDATE_CLIENT)
			.oldClient(oldClient)
			.newClient(newClient)
			.build();
	}


	protected SenderResult getRemoveClientResult(Client oldClient) {
		return new SenderResult.Builder(SenderResult.Status.REMOVE_CLIENT)
			.oldClient(oldClient)
			.build();
	}

}
