package org.jvalue.ceps.data;


import com.fasterxml.jackson.databind.JsonNode;

/**
 * Anything that can accept data from the ODS.
 */
public interface DataSink {

	/**
	 * @param sourceId the source which has new data
	 * @param data the actual (new) data
	 */
	public void onNewData(String sourceId, JsonNode data);

}
