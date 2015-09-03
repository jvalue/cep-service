package org.jvalue.ceps.db;


import com.codahale.metrics.health.HealthCheck;
import com.google.inject.Inject;

import org.jvalue.ceps.db.OdsRegistrationRepository;

/**
 * Checks that CouchDb is reachable.
 */
public final class DbHealthCheck extends HealthCheck {

	private final OdsRegistrationRepository odsRegistrationRepository;

	@Inject
	public DbHealthCheck(OdsRegistrationRepository odsRegistrationRepository ) {
		this.odsRegistrationRepository = odsRegistrationRepository;
	}


	@Override
	public Result check() throws Exception {
		odsRegistrationRepository.getAll();
		return Result.healthy();
	}

}
