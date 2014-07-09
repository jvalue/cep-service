package org.jvalue.ceps.data;

import com.fasterxml.jackson.databind.JsonNode;


public interface DataChangeListener {

	public void onNewData(JsonNode data);

}
