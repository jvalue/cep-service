package org.jvalue.ceps.ods;


import javax.validation.constraints.NotNull;

import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.PUT;
import retrofit.http.Path;

public interface NotificationService {

	static final String URL_NOTIFICATIONS = DataSourceService.URL_DATASOURCES + "/{sourceId}/notifications";

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


	public static class OdsClientDescription {

		private final String type = "HTTP";
		@NotNull private String callbackUrl;
		@NotNull private boolean sendData;

		public OdsClientDescription() { }

		public OdsClientDescription(String callbackUrl, boolean sendData) {
			this.callbackUrl = callbackUrl;
			this.sendData = sendData;
		}

		public String getCallbackUrl() {
			return callbackUrl;
		}

		public boolean getSendData() {
			return sendData;
		}

	}


}
