package org.jvalue.ceps.ods;


import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.PUT;
import retrofit.http.Path;

public interface OdsDataSourceService {

	static final String URL_DATASOURCES = "/datasources";

	@GET(URL_DATASOURCES + "/{sourceId}")
	public OdsDataSource get(@Path("sourceId") String sourceId);


	@PUT(URL_DATASOURCES + "/{sourceId}")
	public OdsDataSource add(@Path("sourceId") String sourceId, @Body OdsDataSourceDescription source);


	@DELETE(URL_DATASOURCES + "/{sourceId}")
	public Response remove(@Path("sourceId") String sourceId);


}
