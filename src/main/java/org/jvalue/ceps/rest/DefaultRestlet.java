package org.jvalue.ceps.rest;

import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.MediaType;
import org.restlet.data.Method;


final class DefaultReslet extends BaseRestlet {


	@Override
	protected void doGet(Request request, Response response) {
		response.setEntity("Nothing here for now. Maybe later?", MediaType.TEXT_PLAIN);
		onSuccess(response);
	}


	@Override
	protected void doPost(Request request, Response response) {
		onInvalidMethod(response, Method.POST);
	}


}
