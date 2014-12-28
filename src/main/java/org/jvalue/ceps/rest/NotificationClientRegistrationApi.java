package org.jvalue.ceps.rest;


import com.google.inject.Inject;

import org.jvalue.ceps.adapter.EplAdapter;
import org.jvalue.ceps.adapter.EplAdapterManager;
import org.jvalue.ceps.notifications.NotificationManager;
import org.jvalue.ceps.notifications.clients.Client;
import org.jvalue.ceps.notifications.clients.GcmClient;

import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/clients")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public final class NotificationClientRegistrationApi {

	private final NotificationManager notificationManager;
	private final EplAdapterManager adapterManager;

	@Inject
	public NotificationClientRegistrationApi(
			NotificationManager notificationManager,
			EplAdapterManager adapterManager) {

		this.notificationManager = notificationManager;
		this.adapterManager = adapterManager;
	}


	@PUT
	@Path("/{adapterName}/{clientId}")
	public Client register(
			@PathParam("adapterName") String adapterName,
			@PathParam("clientId") String clientId,
			@Valid GcmClientDescription clientDescription) {

		EplAdapter adapter = assertIsValidAdapterName(adapterName);
		for (String requiredParam : adapter.getRequiredParams()) {
			if (!clientDescription.eplArguments.containsKey(requiredParam)) throw RestUtils.createJsonFormattedException("missing param " + requiredParam, 400);
		}
		if (adapter.getRequiredParams().size() != clientDescription.eplArguments.size()) throw RestUtils.createJsonFormattedException("found additional param", 400);

		synchronized (this) {
			if (notificationManager.isRegistered(clientId)) throw RestUtils.createJsonFormattedException("already registered", 409);
			Client client = new GcmClient(clientId, clientDescription.deviceId, adapter.toEplStmt(clientDescription.eplArguments));
			notificationManager.register(client);
			return client;
		}
	}


	@DELETE
	@Path("/{adapterName}/{clientId}")
	public void unregister(
			@PathParam("adapterName") String adapterName,
			@PathParam("clientId") String clientId) {

		assertIsValidAdapterName(adapterName);

		synchronized (this) {
			if (!notificationManager.isRegistered(clientId)) throw RestUtils.createJsonFormattedException("not registered", 404);
			notificationManager.unregister(clientId);
		}
	}


	private EplAdapter assertIsValidAdapterName(String adapterName) {
		EplAdapter adapter = adapterManager.getByName(adapterName);
		if (adapter == null) throw RestUtils.createNotFoundException();
		return adapter;
	}


	private static final class GcmClientDescription {

		@NotNull private String deviceId;
		@NotNull private Map<String, String> eplArguments;

	}

}
