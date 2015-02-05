package org.jvalue.ceps.main;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.jvalue.common.db.CouchDbConfig;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import io.dropwizard.Configuration;

public final class CepsConfig extends Configuration {

	@NotNull private final String cepsBaseUrl;
	@NotNull private final String odsBaseUrl;

	@NotNull private final String gcmApiKey;
	private final long gcmGarbageCollectorPeriod;

	private final long eventGarbageCollectorPeriod;
	private final long eventGarbageCollectorMaxAge;

	@NotNull @Valid private final CouchDbConfig couchDb;


	@JsonCreator
	public CepsConfig(
			@JsonProperty("cepsBaseUrl") String cepsBaseUrl,
			@JsonProperty("odsBaseUrl") String odsBaseUrl,
			@JsonProperty("gcmApiKey") String gcmApiKey,
			@JsonProperty("gcmGarbageCollectorPeriod") long gcmGarbageCollectorPeriod,
			@JsonProperty("eventGarbageCollectorPeriod") long eventGarbageCollectorPeriod,
			@JsonProperty("eventGarbageCollectorMaxAge") long eventGarbageCollectorMaxAge,
			@JsonProperty("couchDb") CouchDbConfig couchDb) {

		this.cepsBaseUrl = cepsBaseUrl;
		this.odsBaseUrl = odsBaseUrl;
		this.gcmApiKey = gcmApiKey;
		this.gcmGarbageCollectorPeriod = gcmGarbageCollectorPeriod;
		this.eventGarbageCollectorPeriod = eventGarbageCollectorPeriod;
		this.eventGarbageCollectorMaxAge = eventGarbageCollectorMaxAge;
		this.couchDb = couchDb;
	}


	public String getCepsBaseUrl() {
		return cepsBaseUrl;
	}


	public String getOdsBaseUrl() {
		return odsBaseUrl;
	}


	public long getGcmGarbageCollectorPeriod() {
		return gcmGarbageCollectorPeriod;
	}


	public String getGcmApiKey() {
		return gcmApiKey;
	}


	public long getEventGarbageCollectorPeriod() {
		return eventGarbageCollectorPeriod;
	}


	public long getEventGarbageCollectorMaxAge() {
		return eventGarbageCollectorMaxAge;
	}


	public CouchDbConfig getCouchDb() {
		return couchDb;
	}

}
