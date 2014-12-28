package org.jvalue.ceps.rest;


import com.google.inject.Inject;

import org.jvalue.ceps.event.Event;
import org.jvalue.ceps.event.EventManager;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/events")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public final class EventApi {

	private final EventManager eventManager;

	@Inject
	public EventApi(EventManager eventManager) {
		this.eventManager = eventManager;
	}


	@GET
	public List<Event> getAll() {
		return eventManager.getAll();
	}


	@GET
	@Path("/{eventId}")
	public Event get(@PathParam("eventId") String eventId) {
		return eventManager.getEvent(eventId);
	}


	@DELETE
	@Path("/{eventId}")
	public void remove(@PathParam("eventId") String eventId) {
		eventManager.removeEvent(eventId);
	}

}
