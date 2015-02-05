package org.jvalue.ceps.notifications.sender;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;

import org.jvalue.ceps.api.notifications.HttpClient;

import java.util.List;

import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.JacksonConverter;
import retrofit.http.Body;
import retrofit.http.POST;


final class HttpSender extends NotificationSender<HttpClient> {

	@Inject
	HttpSender() { }


	@Override
	public SenderResult sendEventUpdate(
			HttpClient client,
			String eventId,
			List<JsonNode> newEvents,
			List<JsonNode> oldEvents) {

		RestAdapter adapter = new RestAdapter.Builder()
				.setConverter(new JacksonConverter())
				.setEndpoint(client.getDeviceId())
				.build();
		NewDataCallbackService callbackService = adapter.create(NewDataCallbackService.class);

		EventData content = new EventData(client.getId(), eventId);

		try {
			callbackService.onNewData(content);
			return getSuccessResult();
		} catch (RetrofitError re) {
			return getErrorResult(re);
		}
	}


	/**
	 * The data that will be sent to the client.
	 */
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
	private static final class EventData {

		public final String clientId;
		public final String eventId;

		public EventData(String clientId, String eventId) {
			this.clientId = clientId;
			this.eventId = eventId;
		}

	}


	/**
	 * Describes the REST endpoint that HTTP clients have to implement.
	 */
	private static interface NewDataCallbackService {

		@POST("/")
		public Response onNewData(@Body EventData data);

	}
}
