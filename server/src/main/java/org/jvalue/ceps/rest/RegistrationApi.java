package org.jvalue.ceps.rest;


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
import org.jvalue.commons.auth.RestrictedTo;
import org.jvalue.commons.auth.Role;
import org.jvalue.commons.auth.User;
import org.jvalue.commons.rest.RestUtils;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/notifications/{adapterId}")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public final class RegistrationApi {

	private final NotificationManager notificationManager;
	private final EplAdapterManager adapterManager;

	@Inject
	public RegistrationApi(
			NotificationManager notificationManager,
			EplAdapterManager adapterManager) {

		this.notificationManager = notificationManager;
		this.adapterManager = adapterManager;
	}


	@POST
	public Client registerClient(
			@RestrictedTo(Role.PUBLIC) final User user,
			@PathParam("adapterId") final String adapterId,
			@Valid final ClientDescription clientDescription) {

		final EplAdapter adapter = assertIsValidAdapterId(adapterId);

		// check arg count
		if (adapter.getRequiredArguments().size() != clientDescription.getEplArguments().size()) throw RestUtils.createJsonFormattedException("found additional param", 400);

		// check arg types
		for (String requiredParam : adapter.getRequiredArguments().keySet()) {
			if (!clientDescription.getEplArguments().containsKey(requiredParam)) throw RestUtils.createJsonFormattedException("missing param " + requiredParam, 400);

			ArgumentType argType = adapter.getRequiredArguments().get(requiredParam);
			Object suppliedParam = clientDescription.getEplArguments().get(requiredParam);

			if (!(argType.equals(ArgumentType.NUMBER) && suppliedParam instanceof Number)
					&& !(argType.equals(ArgumentType.STRING) && suppliedParam instanceof String)
					&& !(argType.equals(ArgumentType.BOOLEAN) && suppliedParam instanceof Boolean)) {
				throw RestUtils.createJsonFormattedException("invalid type for param " + requiredParam + ", should be " + argType.name(), 400);
			}
		}

		final String clientId = UUID.randomUUID().toString();
		Client client = clientDescription.accept(new ClientDescriptionVisitor<Void, Client>() {
			@Override
			public Client visit(GcmClientDescription clientDescription, Void param) {
				return new GcmClient(clientId, clientDescription.getDeviceId(), adapterId, clientDescription.getEplArguments(), user.getId());
			}

			@Override
			public Client visit(HttpClientDescription clientDescription, Void param) {
				return new HttpClient(clientId, clientDescription.getDeviceId(), adapterId, clientDescription.getEplArguments(), user.getId());
			}
		}, null);
		notificationManager.register(client);
		return client;
	}


	@DELETE
	@Path("/{clientId}")
	public void unregisterClient(
			@RestrictedTo(Role.PUBLIC) User user,
			@PathParam("adapterId") String adapterId,
			@PathParam("clientId") String clientId) {

		assertIsValidAdapterId(adapterId);

		if (!notificationManager.isRegistered(clientId)) throw RestUtils.createNotFoundException();
		Client client = notificationManager.get(clientId);
		if (!user.getRole().equals(Role.ADMIN) && !user.getId().equals(client.getId()))  throw RestUtils.createNotFoundException();
		notificationManager.unregister(clientId);
	}


	@GET
	@Path("/{clientId}")
	public Client getClient(
			@RestrictedTo(Role.PUBLIC) User user,
			@PathParam("adapterId") String adapterId,
			@PathParam("clientId") String clientId) {

		assertIsValidAdapterId(adapterId);
		Client client = notificationManager.get(clientId);
		if (!user.getRole().equals(Role.ADMIN) && !user.getId().equals(client.getId()))  throw RestUtils.createNotFoundException();
		return client;
	}


	@GET
	public List<Client> getAllClients(
			@RestrictedTo(Role.PUBLIC) User user,
			@PathParam("adapterId") String adapterId) {

		assertIsValidAdapterId(adapterId);
		if (user.getRole().equals(Role.ADMIN)) return notificationManager.getAll();
		else return notificationManager.getAll(user);
	}


	private EplAdapter assertIsValidAdapterId(String adapterId) {
		EplAdapter adapter = adapterManager.get(adapterId);
		if (adapter == null) throw RestUtils.createNotFoundException();
		return adapter;
	}


}
