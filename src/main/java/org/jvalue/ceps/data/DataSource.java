package org.jvalue.ceps.data;

import org.jvalue.ceps.utils.Assert;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
public final class DataSource {

	private final String odsSourceId, odsUrl, odsSchemaUrl;

	@JsonCreator
	public DataSource(
			@JsonProperty("odsSourceId") String odsSourceId, 
			@JsonProperty("odsUrl") String odsUrl,
			@JsonProperty("odsSchemaUrl") String odsSchemaUrl) {

		Assert.assertNotNull(odsSourceId, odsUrl, odsSchemaUrl);
		this.odsSourceId = odsSourceId;
		this.odsUrl = odsUrl;
		this.odsSchemaUrl = odsSchemaUrl;
	}


	public String getOdsSourceId() {
		return odsSourceId;
	}


	public String getOdsUrl() {
		return odsUrl;
	}


	public String getOdsSchemaUrl() {
		return odsSchemaUrl;
	}


	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof DataSource)) return false;
		DataSource source = (DataSource) other;
		return source.odsSourceId.equals(odsSourceId) 
			&& source.odsUrl.equals(odsUrl) 
			&& source.odsSchemaUrl.equals(odsSchemaUrl);
	}


	@Override
	public int hashCode() {
		final int MULT = 17;
		int hash = 13;
		hash = hash + MULT * odsSourceId.hashCode();
		hash = hash + MULT * odsUrl.hashCode();
		hash = hash + MULT * odsSchemaUrl.hashCode();
		return hash;
	}

}
