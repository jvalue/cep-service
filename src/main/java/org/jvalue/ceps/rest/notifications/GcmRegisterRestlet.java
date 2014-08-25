package org.jvalue.ceps.rest.notifications;

import java.util.HashSet;

import org.jvalue.ceps.adapter.ClientAdapter;
import org.jvalue.ceps.notifications.NotificationManager;
import org.jvalue.ceps.notifications.clients.Client;
import org.jvalue.ceps.notifications.clients.ClientFactory;
import org.restlet.Request;


final class GcmRegisterRestlet extends BaseRegisterRestlet {


	protected GcmRegisterRestlet(NotificationManager manager, ClientAdapter clientAdapter) {
		super(manager, clientAdapter, new HashSet<String>());
	}


	@Override
	protected Client getClient(Request request, String deviceId, String eplStmt) {
		return ClientFactory.createGcmClient(deviceId, eplStmt);
	}

}
