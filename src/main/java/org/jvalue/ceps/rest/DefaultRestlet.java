package org.jvalue.ceps.rest;

import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Status;


final class DefaultRestlet extends BaseRestlet {


	@Override
	protected void doGet(Request request, Response response) {
		response.setEntity("Nothing here for now. Maybe later?", MediaType.TEXT_PLAIN);
		response.setStatus(Status.CLIENT_ERROR_NOT_FOUND, "Nothing here");
	}


	@Override
	protected void doPost(Request request, Response response) {
		onInvalidMethod(response, Method.POST);
	}


}
