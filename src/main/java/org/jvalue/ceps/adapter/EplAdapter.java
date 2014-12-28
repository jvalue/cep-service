package org.jvalue.ceps.adapter;

import java.util.Map;
import java.util.Set;


/**
 * Base class for creating an epl stmt based on parameters supplied by
 * end users via the REST api.
 */
public interface EplAdapter {

	/**
	 * @return A epl stmt derived from user supplied parameters.
	 */
	public String toEplStmt(Map<String, String> params);

	/**
	 * @return The parameters that users are required to supply via the REST interface.
	 */
	public Set<String> getRequiredParams();

}
