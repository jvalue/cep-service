package org.jvalue.ceps.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

import org.jvalue.ceps.utils.Assert;

import java.net.URL;


@JsonIgnoreProperties(ignoreUnknown = true)
public final class DataSource {

	private final String sourceId;
	private final URL serverBaseUrl;
	private final String dataSchemaUrl;

	@JsonCreator
	public DataSource(
			@JsonProperty("sourceId") String sourceId,
			@JsonProperty("serverBaseUrl") URL serverBaseUrl,
			@JsonProperty("odsSchemaUrl") String dataSchemaUrl) {

		Assert.assertNotNull(sourceId, serverBaseUrl, dataSchemaUrl);
		this.sourceId = sourceId;
		this.serverBaseUrl = serverBaseUrl;
		this.dataSchemaUrl = dataSchemaUrl;
	}


	public String getSourceId() {
		return sourceId;
	}


	public URL getServerBaseUrl() {
		return serverBaseUrl;
	}


	public String getDataSchemaUrl() {
		return dataSchemaUrl;
	}


	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof DataSource)) return false;
		DataSource source = (DataSource) other;
		return Objects.equal(sourceId, source.sourceId)
				&& Objects.equal(serverBaseUrl, source.serverBaseUrl)
				&& Objects.equal(dataSchemaUrl, source.dataSchemaUrl);
	}


	@Override
	public int hashCode() {
		return Objects.hashCode(sourceId, serverBaseUrl, dataSchemaUrl);
	}

}
