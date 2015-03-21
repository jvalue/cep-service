package org.jvalue.ceps.rest;


import org.ektorp.DocumentNotFoundException;
import org.jvalue.ceps.adapter.EplAdapterManager;
import org.jvalue.ceps.api.adapter.EplAdapter;
import org.jvalue.ceps.api.adapter.EplAdapterDescription;
import org.jvalue.common.auth.RestrictedTo;
import org.jvalue.common.auth.Role;
import org.jvalue.common.auth.User;
import org.jvalue.common.rest.RestUtils;

import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/eplAdapter")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public final class EplAdapterApi {

	private final EplAdapterManager adapterManager;

	@Inject
	public EplAdapterApi(EplAdapterManager adapterManager) {
		this.adapterManager = adapterManager;
	}


	@PUT
	@Path("/{adapterId}")
	public EplAdapter addAdapter(
			@RestrictedTo(Role.ADMIN) User user,
			@PathParam("adapterId") String adapterId,
			@Valid EplAdapterDescription adapterDescription) {

		try {
			adapterManager.get(adapterId);
			throw RestUtils.createJsonFormattedException("adapter already exists", 409);
		} catch (DocumentNotFoundException dnfe) {
			// all good
		}

		EplAdapter adapter = new EplAdapter(adapterId, adapterDescription.getEplBlueprint(), adapterDescription.getRequiredArguments());
		adapterManager.add(adapter);
		return adapter;
	}


	@GET
	@Path("/{adapterId}")
	public EplAdapter getAdapter(@PathParam("adapterId") String adapterId) {
		return adapterManager.get(adapterId);
	}


	@GET
	public List<EplAdapter> getAllAdapters() {
		return adapterManager.getAll();
	}


	@DELETE
	@Path("/{adapterId}")
	public void deleteAdapter(
			@RestrictedTo(Role.ADMIN) User user,
			@PathParam("adapterId") String adapterId) {

		EplAdapter adapter = adapterManager.get(adapterId);
		if (adapter == null) throw RestUtils.createNotFoundException();
		adapterManager.remove(adapter);
	}

}
