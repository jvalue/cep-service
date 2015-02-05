package org.jvalue.ceps.rest;


import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;

import org.jvalue.ceps.adapter.EplAdapterManager;
import org.jvalue.ceps.api.adapter.ArgumentType;
import org.jvalue.ceps.api.adapter.EplAdapter;
import org.jvalue.ceps.api.notifications.Client;
import org.jvalue.ceps.api.notifications.ClientDescription;
import org.jvalue.ceps.api.notifications.ClientDescriptionVisitor;
import org.jvalue.ceps.api.notifications.GcmClient;
import org.jvalue.ceps.api.notifications.GcmClientDescription;
import org.jvalue.ceps.api.notifications.HttpClient;
import org.jvalue.ceps.api.notifications.HttpClientDescription;
import org.jvalue.ceps.notifications.NotificationManager;
import org.jvalue.common.rest.RestUtils;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/notifications")
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
			@PathParam("adapterName") final String adapterName,
			@PathParam("clientId") final String clientId,
			@Valid final ClientDescription clientDescription) {

		final Map<String, Object> adapterArguments = new HashMap<>();
		final EplAdapter adapter = assertIsValidAdapterName(adapterName);

		if (adapter.getRequiredArguments().size() != clientDescription.getEplArguments().size()) throw RestUtils.createJsonFormattedException("found additional param", 400);
		for (String requiredParam : adapter.getRequiredArguments().keySet()) {
			if (!clientDescription.getEplArguments().containsKey(requiredParam)) throw RestUtils.createJsonFormattedException("missing param " + requiredParam, 400);

			ArgumentType argType = adapter.getRequiredArguments().get(requiredParam);
			JsonNode suppliedParam = clientDescription.getEplArguments().get(requiredParam);

			// convert supplied args to correct type
			if (argType.equals(ArgumentType.NUMBER) && suppliedParam.isNumber()) {
				adapterArguments.put(requiredParam, suppliedParam.asDouble());
			} else if (argType.equals(ArgumentType.STRING) && suppliedParam.isTextual()) {
				adapterArguments.put(requiredParam, suppliedParam.asText());
			} else if (argType.equals(ArgumentType.BOOLEAN) && suppliedParam.isBoolean()) {
				adapterArguments.put(requiredParam, suppliedParam.asBoolean());
			} else {
				throw RestUtils.createJsonFormattedException("invalid type for param " + requiredParam + ", should be " + argType.name(), 400);
			}
		}

		synchronized (this) {
			if (notificationManager.isRegistered(clientId)) throw RestUtils.createJsonFormattedException("already registered", 409);
			Client client = clientDescription.accept(new ClientDescriptionVisitor<Void, Client>() {
										 @Override
										 public Client visit(GcmClientDescription clientDescription, Void param) {
											 return new GcmClient(clientId, clientDescription.getDeviceId(), adapterManager.createEplStatement(adapter, adapterArguments));
										 }

										 @Override
										 public Client visit(HttpClientDescription clientDescription, Void param) {
											 return new HttpClient(clientId, clientDescription.getDeviceId(), adapterManager.createEplStatement(adapter, adapterArguments));
										 }
									 }, null);
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
		EplAdapter adapter = adapterManager.get(adapterName);
		if (adapter == null) throw RestUtils.createNotFoundException();
		return adapter;
	}


}
