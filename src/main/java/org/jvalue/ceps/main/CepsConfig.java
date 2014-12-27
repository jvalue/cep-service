package org.jvalue.ceps.main;


import com.fasterxml.jackson.annotation.JsonCreator;

import java.net.URL;

import javax.validation.constraints.NotNull;

import io.dropwizard.Configuration;

public final class CepsConfig extends Configuration {

	@NotNull private final URL odsUrl;

	private final long eventGarbageCollectionPeriod;
	private final long eventGarbageCollectionMaxAge;


	@JsonCreator
	public CepsConfig(
			URL odsUrl,
			long eventGarbageCollectionPeriod,
			long eventGarbageCollectionMaxAge) {

		this.odsUrl = odsUrl;
		this.eventGarbageCollectionPeriod = eventGarbageCollectionPeriod;
		this.eventGarbageCollectionMaxAge = eventGarbageCollectionMaxAge;
	}


	public URL getOdsUrl() {
		return odsUrl;
	}


	public long getEventGarbageCollectionPeriod() {
		return eventGarbageCollectionPeriod;
	}


	public long getEventGarbageCollectionMaxAge() {
		return eventGarbageCollectionMaxAge;
	}

}
