package org.jvalue.ceps.main;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.jvalue.commons.Credentials;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public final class OdsConfig {

	@NotNull private final String url;
	@NotNull @Valid private final Credentials admin;

	@JsonCreator
	public OdsConfig(
			@JsonProperty("url") String url,
			@JsonProperty("admin") Credentials admin) {

		this.url = url;
		this.admin = admin;
	}


	public String getUrl() {
		return url;
	}


	public Credentials getAdmin() {
		return admin;
	}

}
