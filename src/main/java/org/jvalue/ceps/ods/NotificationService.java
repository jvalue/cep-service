package org.jvalue.ceps.ods;


import javax.validation.constraints.NotNull;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.PUT;
import retrofit.http.Path;

public interface NotificationService {

	static final String URL_NOTIFICATIONS = "/datasources/{sourceId}/notifications";

	@PUT(URL_NOTIFICATIONS + "/{clientId}")
	public OdsClient register(
			@Path("sourceId") String sourceId,
			@Path("clientId") String clientId,
			@Body OdsClientDescription clientDescription);


	@GET(URL_NOTIFICATIONS + "/{clientId}")
	public OdsClient get(
			@Path("sourceId") String sourceId,
			@Path("clientId") String clientId);


	public static class OdsClientDescription {

		private final String type = "HTTP";
		@NotNull private String restUrl;
		@NotNull private String sourceParam;
		@NotNull private boolean sendData;

		public OdsClientDescription() { }

		public OdsClientDescription(String restUrl, String sourceParam, boolean sendData) {
			this.restUrl = restUrl;
			this.sourceParam = sourceParam;
			this.sendData = sendData;
		}

		public String getRestUrl() {
			return restUrl;
		}

		public String getSourceParam() {
			return sourceParam;
		}

		public boolean getSendData() {
			return sendData;
		}

	}


	public static final class OdsClient extends OdsClientDescription {

		private String clientId;

		public String getClientId() {
			return clientId;
		}

	}

}
