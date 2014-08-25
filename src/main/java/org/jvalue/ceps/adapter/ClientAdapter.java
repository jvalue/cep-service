package org.jvalue.ceps.adapter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.restlet.Request;
import org.restlet.data.Form;


/**
 * Base class for creating an epl stmt based on parameters supplied by
 * end users via the REST api.
 */
public abstract class ClientAdapter {

	public final String toEplStmt(Request request) {
		Map<String, String> params = new HashMap<String, String>();

		Form queryForm = request.getResourceRef().getQueryAsForm();
		for (String param : getRequiredParams()) {
			String value = queryForm.getFirst(param).getValue();
			params.put(param, value);
		}
		
		return toEplStmt(params);
	}

	/**
	 * @return A epl stmt derived from user supplied parameters.
	 */
	protected abstract String toEplStmt(Map<String, String> params);

	/**
	 * @return The parameters that users are required to supply via the REST interface.
	 */
	public abstract Set<String> getRequiredParams();

}
