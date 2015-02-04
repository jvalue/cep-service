package org.jvalue.ceps.rest;


import com.google.inject.Inject;

import org.jvalue.ceps.data.DataManager;
import org.jvalue.ceps.data.OdsRegistration;
import org.jvalue.common.rest.RestUtils;
import org.jvalue.ods.api.sources.DataSourceApi;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import retrofit.RetrofitError;

@Path("/sources")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public final class SourcesApi {

	private final DataManager dataManager;
	private final DataSourceApi odsSourceApi;

	@Inject
	public SourcesApi(DataManager dataManager, DataSourceApi odsSourceApi) {
		this.dataManager = dataManager;
		this.odsSourceApi = odsSourceApi;
	}


	@POST
	@Path("/{sourceId}")
	public OdsRegistration registerSource(@PathParam("sourceId") String sourceId) {
		try {
			// check that source is registered on ODS
			odsSourceApi.get(sourceId);
		} catch (RetrofitError re) {
			throw RestUtils.createJsonFormattedException("source not registered on ODS", 409);
		}
		if (dataManager.isBeingMonitored(sourceId)) throw RestUtils.createJsonFormattedException("already being monitored", 409);
		return dataManager.startMonitoring(sourceId);
	}


	@GET
	@Path("/{sourceId}")
	public OdsRegistration getRegisteredSource(@PathParam("sourceId") String sourceId) {
		if (!dataManager.isBeingMonitored(sourceId)) throw RestUtils.createNotFoundException();
		return dataManager.get(sourceId);
	}


	@GET
	public List<OdsRegistration> getAllRegisteredSource() {
		return dataManager.getAll();
	}


	@DELETE
	@Path("/{sourceId}")
	public void unregisterSource(@PathParam("sourceId") String sourceId) {
		if (dataManager.isBeingMonitored(sourceId)) throw RestUtils.createJsonFormattedException("already being monitored", 409);
		dataManager.stopMonitoring(sourceId);
	}

}
