package org.jvalue.ceps.esper;

import com.fasterxml.jackson.databind.JsonNode;


public interface DataUpdateListener {

	public void onNewDataType(String dataName, JsonNode dataSchema);
	public void onNewData(String dataName, JsonNode data);

}
