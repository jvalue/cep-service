package org.jvalue.ceps.rest;

import org.restlet.Request;
import org.restlet.data.Status;


final class DefaultRestlet extends BaseRestlet {


	@Override
	protected RestletResult doGet(Request request) {
		return RestletResult.newErrorResult(
				Status.CLIENT_ERROR_NOT_FOUND, 
				"Nothing here for now. Maybe later?");
	}


}
