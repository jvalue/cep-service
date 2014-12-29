package org.jvalue.ceps.adapter;

import java.util.Map;


/**
 * Base class for creating an epl stmt based on parameters supplied by
 * end users via the REST api.
 */
public interface EplAdapter {

	/**
	 * @return A epl stmt derived from user supplied parameters.
	 */
	public String toEplStmt(Map<String, Object> params);

	/**
	 * @return The parameters that users are required to supply via the REST interface.
	 */
	public Map<String, Class<?>> getRequiredParams();

	/**
	 * @return a human readable URL encoded name for this adapter. Will be used by the REST api to
	 * create an endpoint for this adapter.
	 */
	public String getName();

}
