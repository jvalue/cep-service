package org.jvalue.ceps.api.data;


import java.util.List;

import retrofit.client.Response;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.PUT;
import retrofit.http.Path;

public interface DataApi {

	static final String URL_DATA = "/sources";

	@PUT(URL_DATA + "/{sourceId}")
	public OdsRegistration addSource(@Path("sourceId") String sourceId);


	@DELETE(URL_DATA + "/{clientId}")
	public Response removeSource(@Path("sourceId") String sourceId);


	@GET(URL_DATA + "/{clientId}")
	public OdsRegistration getSource(@Path("sourceId") String sourceId);


	@GET(URL_DATA)
	public List<OdsRegistration> getAllSources();

}
