package org.jvalue.ceps.db;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import org.ektorp.CouchDbConnector;
import org.ektorp.DocumentNotFoundException;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.View;
import org.jvalue.ceps.api.event.Event;
import org.jvalue.common.db.DbDocument;
import org.jvalue.common.db.DbDocumentAdaptable;
import org.jvalue.common.db.RepositoryAdapter;

import java.util.List;

public final class EventRepository extends RepositoryAdapter<
		EventRepository.EventCouchDbRepository,
		EventRepository.EventDocument,
		Event> {

	static final String DATABASE_NAME = "events";
	private static final String DOCUMENT_ID = "doc.value.id != null && doc.value.timestamp != null";

	@Inject
	EventRepository(@Named(DATABASE_NAME) CouchDbConnector connector) {
		super(new EventCouchDbRepository(connector));
	}


	@View( name = "all", map = "function(doc) { if (" + DOCUMENT_ID + ") emit(null, doc)}")
	static final class EventCouchDbRepository
			extends CouchDbRepositorySupport<EventDocument>
			implements DbDocumentAdaptable<EventDocument, Event> {

		@Inject
		EventCouchDbRepository(CouchDbConnector connector) {
			super(EventDocument.class, connector);
			initStandardDesignDocument();
		}


		@View(name = "by_id", map = "function(doc) { if (" + DOCUMENT_ID + ") emit(doc.value.id, doc._id) }")
		public EventDocument findById(String eventId) {
			List<EventDocument> events = queryView("by_id", eventId);
			if (events.isEmpty()) throw new DocumentNotFoundException("no event with id " + eventId);
			if (events.size() == 1) return events.get(0);
			throw new IllegalStateException("found more than one event for id " + eventId);
		}


		@Override
		public EventDocument createDbDocument(Event event) {
			return new EventDocument(event);
		}


		@Override
		public String getIdForValue(Event event) {
			return event.getId();
		}

	}


	static final class EventDocument extends DbDocument<Event> {

		@JsonCreator
		public EventDocument(
				@JsonProperty("value") Event event) {
			super(event);
		}

	}

}
