package org.jvalue.ceps.api.adapter;


import java.util.List;

import retrofit.client.Response;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.PUT;
import retrofit.http.Path;

public interface EplAdapterApi {

	static final String URL_ADAPTER = "/eplAdapter";

	@PUT(URL_ADAPTER + "/{adapterId}")
	public EplAdapter addAdapter(@Path("adapterId") String adapterId, EplAdapterDescription adapterDescription);


	@DELETE(URL_ADAPTER + "/{adapterId}")
	public Response removeAdapter(@Path("adapterId") String adapterId);


	@GET(URL_ADAPTER + "/{adapterId}")
	public EplAdapter getAdapter(@Path("adapterId") String adapterId);


	@GET(URL_ADAPTER)
	public List<EplAdapter> getAllAdapter();

}
