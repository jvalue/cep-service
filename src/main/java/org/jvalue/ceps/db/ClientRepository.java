package org.jvalue.ceps.db;


import com.google.inject.Inject;
import com.google.inject.name.Named;

import org.ektorp.CouchDbConnector;
import org.ektorp.DocumentNotFoundException;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.GenerateView;
import org.ektorp.support.View;
import org.jvalue.ceps.notifications.clients.Client;

import java.util.List;

@View( name = "all", map = "function(doc) { if (doc.clientId) emit(null, doc)}")
public final class ClientRepository extends CouchDbRepositorySupport<Client> {

	static final String DATABASE_NAME = "clients";

	@Inject
	ClientRepository(@Named(DATABASE_NAME) CouchDbConnector connector) {
		super(Client.class, connector);
		initStandardDesignDocument();
	}


	@GenerateView
	public Client findByClientId(String clientId) {
		List<Client> clients = queryView("by_clientId", clientId);
		if (clients.isEmpty()) throw new DocumentNotFoundException("no client with clientId" + clientId);
		if (clients.size() == 1) return clients.get(0);
		throw new IllegalStateException("found more than one client for clientId " + clientId);
	}


	@GenerateView
	public List<Client> findByDeviceId(String deviceId) {
		return queryView("by_deviceId", deviceId);
	}

}
