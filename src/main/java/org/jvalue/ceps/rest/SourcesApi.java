package org.jvalue.ceps.rest;


import com.google.inject.Inject;

import org.jvalue.ceps.data.DataManager;
import org.jvalue.ceps.data.OdsRegistration;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/sources")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public final class SourcesApi {

	private final DataManager dataManager;

	@Inject
	public SourcesApi(DataManager dataManager) {
		this.dataManager = dataManager;
	}


	@POST
	@Path("/{sourceId}")
	public OdsRegistration registerSource(@PathParam("sourceId") String sourceId) {
		if (dataManager.isBeingMonitored(sourceId)) RestUtils.createJsonFormattedException("already being monitored", 409);
		return dataManager.startMonitoring(sourceId);
	}


	@GET
	@Path("/{sourceId}")
	public OdsRegistration getRegisteredSource(@PathParam("sourceId") String sourceId) {
		if (!dataManager.isBeingMonitored(sourceId))  RestUtils.createNotFoundException();
		return dataManager.get(sourceId);
	}


	@GET
	public List<OdsRegistration> getAllRegisteredSource() {
		return dataManager.getAll();
	}


	@DELETE
	@Path("/{sourceId}")
	public void unregisterSource(@PathParam("sourceId") String sourceId) {
		if (dataManager.isBeingMonitored(sourceId)) RestUtils.createJsonFormattedException("already being monitored", 409);
		dataManager.stopMonitoring(sourceId);
	}

}
