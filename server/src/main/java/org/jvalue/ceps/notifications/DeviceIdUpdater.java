package org.jvalue.ceps.notifications;


import org.jvalue.ceps.api.notifications.Client;
import org.jvalue.ceps.api.notifications.ClientVisitor;
import org.jvalue.ceps.api.notifications.GcmClient;
import org.jvalue.ceps.api.notifications.HttpClient;

public final class DeviceIdUpdater implements ClientVisitor<String, Client> {

	@Override
	public Client visit(GcmClient client, String newDeviceId) {
		return new GcmClient(client.getId(), newDeviceId, client.getEplStmt());
	}


	@Override
	public Client visit(HttpClient client, String newDeviceId) {
		return new HttpClient(client.getId(), newDeviceId, client.getEplStmt());
	}

}
