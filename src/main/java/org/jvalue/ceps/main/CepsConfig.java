package org.jvalue.ceps.main;


import com.fasterxml.jackson.annotation.JsonCreator;

import java.net.URL;

import javax.validation.constraints.NotNull;

import io.dropwizard.Configuration;

public final class CepsConfig extends Configuration {

	@NotNull private final URL odsUrl;

	@NotNull private final String gcmApiKey;
	private final long gcmGarbageCollectorPeriod;

	private final long eventGarbageCollectionPeriod;
	private final long eventGarbageCollectionMaxAge;


	@JsonCreator
	public CepsConfig(
			URL odsUrl,
			String gcmApiKey,
			long gcmGarbageCollectorPeriod,
			long eventGarbageCollectionPeriod,
			long eventGarbageCollectionMaxAge) {

		this.odsUrl = odsUrl;
		this.gcmApiKey = gcmApiKey;
		this.gcmGarbageCollectorPeriod = gcmGarbageCollectorPeriod;
		this.eventGarbageCollectionPeriod = eventGarbageCollectionPeriod;
		this.eventGarbageCollectionMaxAge = eventGarbageCollectionMaxAge;
	}


	public URL getOdsUrl() {
		return odsUrl;
	}


	public long getGcmGarbageCollectorPeriod() {
		return gcmGarbageCollectorPeriod;
	}


	public String getGcmApiKey() {
		return gcmApiKey;
	}


	public long getEventGarbageCollectionPeriod() {
		return eventGarbageCollectionPeriod;
	}


	public long getEventGarbageCollectionMaxAge() {
		return eventGarbageCollectionMaxAge;
	}

}
