package org.jvalue.ceps.api.notifications;


import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.PUT;
import retrofit.http.Path;

public interface NotificationApi {

	static final String URL_NOTIFICATIONS = "/notifications";

	@PUT(URL_NOTIFICATIONS + "/{adapterName}/{clientId}")
	public Client register(
			@Path("sourceId") String sourceId,
			@Path("clientId") String clientId,
			@Body ClientDescription clientDescription);


	@DELETE(URL_NOTIFICATIONS + "/{adapterName}/{clientId}")
	public Client unregister(
			@Path("sourceId") String sourceId,
			@Path("clientId") String clientId);

}
