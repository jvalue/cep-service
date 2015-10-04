package org.jvalue.ceps.pegelalarm;

import com.codahale.metrics.health.HealthCheck;

import org.jvalue.ceps.adapter.EplAdapterManager;

import javax.inject.Inject;

/**
 * Ensures that all EPL adapter required by PegelAlarm are registered.
 */
public class EplAdapterHealthCheck extends HealthCheck {

	private final EplAdapterManager eplAdapterManager;

	@Inject
	EplAdapterHealthCheck(EplAdapterManager eplAdapterManager) {
		this.eplAdapterManager = eplAdapterManager;
	}

	@Override
	protected Result check() throws Exception {
		if (eplAdapterManager.get("pegelAlarmAboveLevel") == null
				|| eplAdapterManager.get("pegelAlarmBelowLevel") == null
				|| eplAdapterManager.get("pegelAlarmAboveOrBelowLevel") == null) {
			return Result.unhealthy("missing epl adapter");
		}
		return Result.healthy();
	}

}
