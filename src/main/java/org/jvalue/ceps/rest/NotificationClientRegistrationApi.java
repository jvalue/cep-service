package org.jvalue.ceps.rest;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import org.jvalue.ceps.adapter.AdapterModule;
import org.jvalue.ceps.adapter.EplAdapter;
import org.jvalue.ceps.notifications.NotificationManager;
import org.jvalue.ceps.notifications.clients.Client;
import org.jvalue.ceps.notifications.clients.ClientFactory;

import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/clients")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public final class NotificationClientRegistrationApi {

	private final NotificationManager notificationManager;
	private final EplAdapter pegelOnlineAdapter;

	@Inject
	public NotificationClientRegistrationApi(
			NotificationManager notificationManager,
			@Named(AdapterModule.ADAPTER_PEGELONLINE) EplAdapter pegelOnlineAdapter) {

		this.notificationManager = notificationManager;
		this.pegelOnlineAdapter = pegelOnlineAdapter;
	}


	@POST
	public Client register(GcmClientDescription clientDescription) {
		Client client = ClientFactory.createGcmClient(clientDescription.deviceId, pegelOnlineAdapter.toEplStmt(clientDescription.eplArguments));
		notificationManager.register(client);
		return client;
	}


	@POST
	public void unregister(String clientId) {
		notificationManager.unregister(clientId);
	}


	private static final class GcmClientDescription {

		private final String deviceId;
		private final Map<String, String> eplArguments;

		@JsonCreator
		public GcmClientDescription(
				@JsonProperty("deviceId") String deviceId,
				@JsonProperty("eplArguments") Map<String, String> eplArguments) {

			this.deviceId = deviceId;
			this.eplArguments = eplArguments;
		}

	}

}
