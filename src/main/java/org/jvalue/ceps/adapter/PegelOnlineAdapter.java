package org.jvalue.ceps.adapter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


final class PegelOnlineAdapter implements EplAdapter {

	private static final String
		KEY_RIVER = "river",
		KEY_STATION = "station",
		KEY_LEVEL = "level",
		KEY_ABOVE = "above";

	private static final Set<String> requiredParams;

	static {
		Set<String> params = new HashSet<String>();
		params.add(KEY_RIVER);
		params.add(KEY_STATION);
		params.add(KEY_LEVEL);
		params.add(KEY_ABOVE);
		requiredParams = Collections.unmodifiableSet(params);
	}


	@Override
	public Set<String> getRequiredParams() {
		return requiredParams;
	}


	@Override
	public String toEplStmt(Map<String, String> params) {
		String river = params.get(KEY_RIVER);
		String station = params.get(KEY_STATION);
		double level = Double.valueOf(params.get(KEY_LEVEL));
		boolean alarmWhenAbove = Boolean.valueOf(params.get(KEY_ABOVE));

		String filter = "(longname = '" + station + "' "
			+ "and BodyOfWater.longname = '" + river + "' "
			+ "and timeseries.firstof(i => i.shortname = 'W') is not null)";

		String object1 = "measurement1";
		String object2 = "measurement2";
		String dataType = "`de-pegelonline`";

		return "select " + object1 + ", " + object2 + " from pattern [every "
			+ object1 + "=" + dataType + filter + " -> " + object2 + "=" + dataType + filter + "]"
			+ " where " + getWhere(object1, !alarmWhenAbove, level)
			+ " and " + getWhere(object2, alarmWhenAbove, level);
	}


	private String getWhere(String objectName, boolean greater, double level) {
		String comparison = null; 
		if (greater) comparison = ">=";
		else comparison = "<";

		return objectName + ".timeseries.firstof(i => i.shortname = 'W' "
			+ "and i.currentMeasurement.value " + comparison + " " + level + ") is not null";
	}

}
