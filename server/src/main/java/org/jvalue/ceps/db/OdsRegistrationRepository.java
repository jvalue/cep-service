package org.jvalue.ceps.db;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import org.ektorp.CouchDbConnector;
import org.ektorp.DocumentNotFoundException;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.View;
import org.jvalue.ceps.data.OdsRegistration;
import org.jvalue.common.db.DbDocument;
import org.jvalue.common.db.DbDocumentAdaptable;
import org.jvalue.common.db.RepositoryAdapter;

import java.util.List;


public final class OdsRegistrationRepository extends RepositoryAdapter<
		OdsRegistrationRepository.OdsRegistrationCouchDbRepository,
		OdsRegistrationRepository.OdsRegistrationDocument,
		OdsRegistration> {

	static final String DATABASE_NAME = "sources";
	private static final String DOCUMENT_ID = "doc.value.id != null && doc.value.dataSource != null";

	@Inject
	OdsRegistrationRepository(@Named(DATABASE_NAME) CouchDbConnector connector) {
		super(new OdsRegistrationRepository.OdsRegistrationCouchDbRepository(connector));
	}


	@View( name = "all", map = "function(doc) { if (" + DOCUMENT_ID + ") emit(null, doc)}")
	static final class OdsRegistrationCouchDbRepository
			extends CouchDbRepositorySupport<OdsRegistrationDocument>
			implements DbDocumentAdaptable<OdsRegistrationDocument, OdsRegistration> {

		OdsRegistrationCouchDbRepository(CouchDbConnector connector) {
			super(OdsRegistrationDocument.class, connector);
			initStandardDesignDocument();
		}


		@View(name = "by_id", map = "function(doc) { if (" + DOCUMENT_ID + ") emit(doc.value.id, doc._id) }")
		public OdsRegistrationDocument findById(String eventId) {
			List<OdsRegistrationDocument> events = queryView("by_id", eventId);
			if (events.isEmpty()) throw new DocumentNotFoundException("no registration with id " + eventId);
			if (events.size() == 1) return events.get(0);
			throw new IllegalStateException("found more than one registration for id " + eventId);
		}


		@Override
		public OdsRegistrationDocument createDbDocument(OdsRegistration registration) {
			return new OdsRegistrationDocument(registration);
		}


		@Override
		public String getIdForValue(OdsRegistration registration) {
			return registration.getId();
		}

	}


	static final class OdsRegistrationDocument extends DbDocument<OdsRegistration> {

		@JsonCreator
		public OdsRegistrationDocument(
				@JsonProperty("value") OdsRegistration odsRegistration) {
			super(odsRegistration);
		}

	}

}
