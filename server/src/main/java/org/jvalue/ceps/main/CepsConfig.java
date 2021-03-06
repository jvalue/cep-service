package org.jvalue.ceps.main;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.jvalue.commons.auth.AuthConfig;
import org.jvalue.commons.couchdb.CouchDbConfig;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import io.dropwizard.Configuration;

public final class CepsConfig extends Configuration {

	@NotNull private final String url;

	@NotNull private final String gcmApiKey;
	private final long gcmGarbageCollectorPeriod;

	private final long eventGarbageCollectorPeriod;
	private final long eventGarbageCollectorMaxAge;

	@NotNull @Valid private final AuthConfig auth;
	@NotNull @Valid private final OdsConfig ods;
	@NotNull @Valid private final CouchDbConfig couchDb;


	@JsonCreator
	public CepsConfig(
			@JsonProperty("url") String url,
			@JsonProperty("gcmApiKey") String gcmApiKey,
			@JsonProperty("gcmGarbageCollectorPeriod") long gcmGarbageCollectorPeriod,
			@JsonProperty("eventGarbageCollectorPeriod") long eventGarbageCollectorPeriod,
			@JsonProperty("eventGarbageCollectorMaxAge") long eventGarbageCollectorMaxAge,
			@JsonProperty("auth") AuthConfig auth,
			@JsonProperty("ods") OdsConfig ods,
			@JsonProperty("couchDb") CouchDbConfig couchDb) {

		this.url = url;
		this.gcmApiKey = gcmApiKey;
		this.gcmGarbageCollectorPeriod = gcmGarbageCollectorPeriod;
		this.eventGarbageCollectorPeriod = eventGarbageCollectorPeriod;
		this.eventGarbageCollectorMaxAge = eventGarbageCollectorMaxAge;
		this.auth = auth;
		this.ods = ods;
		this.couchDb = couchDb;
	}


	public String getUrl() {
		return url;
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


	public AuthConfig getAuth() {
		return auth;
	}


	public OdsConfig getOds() {
		return ods;
	}


	public CouchDbConfig getCouchDb() {
		return couchDb;
	}

}
