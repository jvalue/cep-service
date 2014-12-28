package org.jvalue.ceps.ods;


import retrofit.http.GET;
import retrofit.http.Path;

public interface DataSourceService {

	static final String URL_DATASOURCES = "/datasources";

	@GET(URL_DATASOURCES + "/{sourceId}")
	public OdsDataSource get(@Path("sourceId") String sourceId);


}
