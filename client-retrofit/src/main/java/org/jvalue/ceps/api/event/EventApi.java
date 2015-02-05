package org.jvalue.ceps.api.event;

import java.util.List;

import retrofit.client.Response;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Path;


public interface EventApi {

	static final String URL_EVENTS = "/events";


	@DELETE(URL_EVENTS + "/{eventId}")
	public Response removeEvent(@Path("eventId") String eventId);


	@GET(URL_EVENTS + "/{eventId}")
	public Event getEvent(@Path("eventId") String eventId);


	@GET(URL_EVENTS)
	public List<Event> getAllEvents();
}
