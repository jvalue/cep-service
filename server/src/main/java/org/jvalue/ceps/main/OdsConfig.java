package org.jvalue.ceps.main;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.jvalue.commons.auth.BasicCredentials;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public final class OdsConfig {

	@NotNull private final String url;
	@NotNull @Valid private final BasicCredentials admin;

	@JsonCreator
	public OdsConfig(
			@JsonProperty("url") String url,
			@JsonProperty("admin") BasicCredentials admin) {

		this.url = url;
		this.admin = admin;
	}


	public String getUrl() {
		return url;
	}


	public BasicCredentials getAdmin() {
		return admin;
	}

}
