package org.jvalue.ceps.data;

import com.fasterxml.jackson.databind.JsonNode;


public interface DataChangeListener {

	public void onNewDataType(String dataName, JsonNode dataSchema);
	public void onNewData(String dataName, JsonNode data);

}
