package org.jvalue.ceps.data;

import org.jvalue.ceps.utils.Assert;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
public final class DataSource {

	private final String odsId, odsUrl;

	@JsonCreator
	public DataSource(
			@JsonProperty("odsId") String odsId, 
			@JsonProperty("odsUrl") String odsUrl) {

		Assert.assertNotNull(odsId, odsUrl);
		this.odsId = odsId;
		this.odsUrl = odsUrl;
	}


	public String getOdsId() {
		return odsId;
	}


	public String getOdsUrl() {
		return odsUrl;
	}


	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof DataSource)) return false;
		DataSource source = (DataSource) other;
		return source.odsId.equals(odsId) && source.odsUrl.equals(odsUrl);
	}


	@Override
	public int hashCode() {
		final int MULT = 17;
		int hash = 13;
		hash = hash + MULT * odsId.hashCode();
		hash = hash + MULT * odsUrl.hashCode();
		return hash;
	}

}
