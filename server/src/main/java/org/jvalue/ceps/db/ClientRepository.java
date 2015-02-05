package org.jvalue.ceps.db;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import org.ektorp.CouchDbConnector;
import org.ektorp.DocumentNotFoundException;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.View;
import org.jvalue.ceps.api.notifications.Client;
import org.jvalue.common.db.DbDocument;
import org.jvalue.common.db.DbDocumentAdaptable;
import org.jvalue.common.db.RepositoryAdapter;

import java.util.LinkedList;
import java.util.List;

public final class ClientRepository extends RepositoryAdapter<
		ClientRepository.ClientCouchDbRepository,
		ClientRepository.ClientDocument,
		Client> {

	static final String DATABASE_NAME = "clients";
	private static final String DOCUMENT_ID = "doc.value.id != null && doc.value.deviceId != null";

	@Inject
	ClientRepository(@Named(DATABASE_NAME) CouchDbConnector connector) {
		super(new ClientCouchDbRepository(connector));
	}


	public List<Client> findByDeviceId(String deviceId) {
		List<Client> clients = new LinkedList<>();
		for (ClientDocument document : repository.findByDeviceId(deviceId)) clients.add(document.getValue());
		return clients;
	}


	@View( name = "all", map = "function(doc) { if (" + DOCUMENT_ID + ") emit(null, doc) }")
	static final class ClientCouchDbRepository
			extends CouchDbRepositorySupport<ClientDocument>
			implements DbDocumentAdaptable<ClientDocument, Client> {


		ClientCouchDbRepository(CouchDbConnector connector) {
			super(ClientDocument.class, connector);
			initStandardDesignDocument();
		}


		@Override
		@View(name = "by_id", map = "function(doc) { if (" + DOCUMENT_ID + ") emit(doc.value.id, doc._id) }")
		public ClientDocument findById(String clientId) {
			List<ClientDocument> clients = queryView("by_id", clientId);
			if (clients.isEmpty())
				throw new DocumentNotFoundException("no client with id " + clientId);
			if (clients.size() == 1) return clients.get(0);
			throw new IllegalStateException("found more than one client for id " + clientId);
		}


		@View(name = "by_deviceId", map = "function(doc) { if (" + DOCUMENT_ID + ") emit(doc.value.deviceId, doc._id) }")
		public List<ClientDocument> findByDeviceId(String deviceId) {
			return queryView("by_deviceId", deviceId);
		}


		@Override
		public ClientDocument createDbDocument(Client client) {
			return new ClientDocument(client);
		}


		@Override
		public String getIdForValue(Client client) {
			return client.getId();
		}

	}

	static final class ClientDocument extends DbDocument<Client> {

		@JsonCreator
		public ClientDocument(
				@JsonProperty("value") Client client) {
			super(client);
		}

	}
}
