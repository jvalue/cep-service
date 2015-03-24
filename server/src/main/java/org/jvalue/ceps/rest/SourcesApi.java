package org.jvalue.ceps.rest;


import com.google.inject.Inject;

import org.jvalue.ceps.api.data.OdsRegistration;
import org.jvalue.ceps.data.DataManager;
import org.jvalue.commons.auth.RestrictedTo;
import org.jvalue.commons.auth.Role;
import org.jvalue.commons.auth.User;
import org.jvalue.commons.rest.RestUtils;
import org.jvalue.ods.api.DataSourceApi;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import retrofit.RetrofitError;

@Path("/sources")
@Produces(MediaType.APPLICATION_JSON)
public final class SourcesApi {

	private final DataManager dataManager;
	private final DataSourceApi odsSourceApi;

	@Inject
	public SourcesApi(DataManager dataManager, DataSourceApi odsSourceApi) {
		this.dataManager = dataManager;
		this.odsSourceApi = odsSourceApi;
	}


	@PUT
	@Path("/{sourceId}")
	public OdsRegistration addSource(
			@RestrictedTo(Role.ADMIN) User user,
			@PathParam("sourceId") String sourceId) {

		try {
			// check that source is registered on ODS
			odsSourceApi.getSource(sourceId);
		} catch (RetrofitError re) {
			throw RestUtils.createJsonFormattedException("source not registered on ODS", 409);
		}
		if (dataManager.isBeingMonitored(sourceId)) throw RestUtils.createJsonFormattedException("already being monitored", 409);
		return dataManager.startMonitoring(sourceId);
	}


	@GET
	@Path("/{sourceId}")
	public OdsRegistration getSource(@PathParam("sourceId") String sourceId) {
		if (!dataManager.isBeingMonitored(sourceId)) throw RestUtils.createNotFoundException();
		return dataManager.get(sourceId);
	}


	@GET
	public List<OdsRegistration> getAllSources() {
		return dataManager.getAll();
	}


	@DELETE
	@Path("/{sourceId}")
	public void deleteSource(
			@RestrictedTo(Role.ADMIN) User user,
			@PathParam("sourceId") String sourceId) {

		if (!dataManager.isBeingMonitored(sourceId)) throw RestUtils.createNotFoundException();
		dataManager.stopMonitoring(sourceId);
	}

}
