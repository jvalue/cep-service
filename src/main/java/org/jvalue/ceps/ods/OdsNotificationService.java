package org.jvalue.ceps.ods;


import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.PUT;
import retrofit.http.Path;

public interface OdsNotificationService {

	static final String URL_NOTIFICATIONS = OdsDataSourceService.URL_DATASOURCES + "/{sourceId}/notifications";

	@PUT(URL_NOTIFICATIONS + "/{clientId}")
	public OdsClient register(
			@Path("sourceId") String sourceId,
			@Path("clientId") String clientId,
			@Body OdsClientDescription clientDescription);


	@GET(URL_NOTIFICATIONS + "/{clientId}")
	public OdsClient get(
			@Path("sourceId") String sourceId,
			@Path("clientId") String clientId);


	@DELETE(URL_NOTIFICATIONS + "/{clientId}")
	public OdsClient unregister(
			@Path("sourceId") String sourceId,
			@Path("clientId") String clientId);


}
