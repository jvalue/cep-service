package org.jvalue.ceps.ods;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;

import retrofit.http.GET;
import retrofit.http.Path;

public interface DataSourceService {

	static final String URL_DATASOURCES = "/datasources";

	@GET(URL_DATASOURCES + "/{sourceId}")
	public DataSource get(@Path("sourceId") String sourceId);


	@JsonIgnoreProperties(ignoreUnknown = true)
	public static final class DataSource {

		private String id;
		private JsonNode schema;

		private DataSource() { }

		public String getId() {
			return id;
		}

		public JsonNode getSchema() {
			return schema;
		}

	}

}
