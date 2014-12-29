package org.jvalue.ceps.data;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Map;


/**
 * Describes an object which is capable of receiving schema and data information.
 */
public interface DataUpdateListener {

	/**
	 * Called whenever a source has been added / a new schema becomes available.
	 */
	public void onSourceAdded(String sourceId, JsonNode schema);

	/**
	 * Called whenever a source has been removed.
	 */
	public void onSourceRemoved(String sourceId, JsonNode schema);

	/**
	 * Called when new data for source comes availabe.
	 */
	public void onNewSourceData(String sourceId, JsonNode data);

	/**
	 * Used to restore previously added sources.
	 * @param sources mapping sourceIds to source schemas.
	 */
	public void onRestoreSources(Map<String, JsonNode> sources);

}
