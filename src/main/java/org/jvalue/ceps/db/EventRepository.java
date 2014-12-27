package org.jvalue.ceps.db;


import com.google.inject.Inject;
import com.google.inject.name.Named;

import org.ektorp.CouchDbConnector;
import org.ektorp.DocumentNotFoundException;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.GenerateView;
import org.ektorp.support.View;
import org.jvalue.ceps.event.Event;

import java.util.List;

@View( name = "all", map = "function(doc) { if (doc.eventId) emit(null, doc)}")
public final class EventRepository extends CouchDbRepositorySupport<Event> {

	static final String DATABASE_NAME = "events";

	@Inject
	EventRepository(@Named(DATABASE_NAME) CouchDbConnector connector) {
		super(Event.class, connector);
		initStandardDesignDocument();
	}


	@GenerateView
	public Event findByEventId(String eventId) {
		List<Event> events = queryView("by_eventId", eventId);
		if (events.isEmpty()) throw new DocumentNotFoundException("no event with eventId " + eventId);
		if (events.size() == 1) return events.get(0);
		throw new IllegalStateException("found more than one event for eventId " + eventId);
	}

}
