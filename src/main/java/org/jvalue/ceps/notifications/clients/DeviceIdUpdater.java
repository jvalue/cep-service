package org.jvalue.ceps.notifications.clients;


public final class DeviceIdUpdater implements ClientVisitor<String, Client> {

	@Override
	public Client visit(GcmClient client, String newDeviceId) {
		return new GcmClient(client.getId(), newDeviceId, client.getEplStmt());
	}

}
