package org.jvalue.ceps.main;


import com.fasterxml.jackson.annotation.JsonCreator;

import java.net.URL;

import javax.validation.constraints.NotNull;

import io.dropwizard.Configuration;

public final class CepsConfig extends Configuration {

	@NotNull
	private final URL odsUrl;


	@JsonCreator
	public CepsConfig(URL odsUrl) {
		this.odsUrl = odsUrl;
	}


	public URL getOdsUrl() {
		return odsUrl;
	}

}
