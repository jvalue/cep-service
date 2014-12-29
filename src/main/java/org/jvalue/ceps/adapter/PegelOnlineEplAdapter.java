package org.jvalue.ceps.adapter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


final class PegelOnlineEplAdapter extends AbstractEplAdapter {

	private static final String
		KEY_RIVER = "river",
		KEY_STATION = "station",
		KEY_LEVEL = "level",
		KEY_ABOVE = "above";

	private static final Map<String, Class<?>> requiredParams;

	static {
		Map<String, Class<?>> params = new HashMap<>();
		params.put(KEY_RIVER, String.class);
		params.put(KEY_STATION, String.class);
		params.put(KEY_LEVEL, double.class);
		params.put(KEY_ABOVE, boolean.class);
		requiredParams = Collections.unmodifiableMap(params);
	}


	PegelOnlineEplAdapter() {
		super("pegelOnline", requiredParams);
	}


	@Override
	protected String doToEplStmt(Map<String, Object> params) {
		String river = params.get(KEY_RIVER).toString();
		String station =  params.get(KEY_STATION).toString();
		double level = Double.valueOf(params.get(KEY_LEVEL).toString());
		boolean alarmWhenAbove = Boolean.valueOf(params.get(KEY_ABOVE).toString());

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
