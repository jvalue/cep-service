package org.jvalue.ceps.pegelalarm;

import com.codahale.metrics.health.HealthCheck;

import org.jvalue.ceps.api.data.OdsRegistration;
import org.jvalue.ceps.data.DataManager;

import javax.inject.Inject;

/**
 * Asserts that the Pegel Alarm source has been registered.
 */
public class DataSourceHealthCheck extends HealthCheck implements Constants {

	private final DataManager dataManager;

	@Inject
	DataSourceHealthCheck(DataManager dataManager) {
		this.dataManager = dataManager;
	}

	@Override
	protected Result check() throws Exception {
		OdsRegistration registration = dataManager.get(DATA_SOURCE_ID);
		if (!registration.getDataSource().getId().equals(DATA_SOURCE_ID)) return Result.unhealthy("failed to find source with id " + DATA_SOURCE_ID);
		return Result.healthy();
	}

}
