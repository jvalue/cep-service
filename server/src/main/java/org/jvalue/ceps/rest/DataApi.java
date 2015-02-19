package org.jvalue.ceps.rest;


import com.google.inject.Inject;

import org.jvalue.ceps.api.data.SourceData;
import org.jvalue.ceps.data.DataSink;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/data")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public final class DataApi {

	static final String URL_DATA = "/data";

	private final DataSink dataSink;

	@Inject
	public DataApi(DataSink dataSink) {
		this.dataSink = dataSink;
	}


	@POST
	public void onNewData(SourceData sourceData) {
		dataSink.onNewData(sourceData.getSourceId(), sourceData.getData());
	}


}
