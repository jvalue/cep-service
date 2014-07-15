package org.jvalue.ceps.rest;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.jvalue.ceps.utils.Assert;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.Method;
import org.restlet.data.Parameter;
import org.restlet.data.Status;


public abstract class BaseRestlet extends Restlet {

	private final Set<String> mandatoryQueryParams, optionalQueryParams;

	protected BaseRestlet(
			Set<String> mandatoryQueryParams,
			Set<String> optionalQueryParams) {

		Assert.assertNotNull(mandatoryQueryParams, optionalQueryParams);
		Assert.assertTrue(
				Collections.disjoint(mandatoryQueryParams, optionalQueryParams),
				"param sets must be disjoint");

		this.mandatoryQueryParams = mandatoryQueryParams;
		this.optionalQueryParams = optionalQueryParams;
	}


	protected BaseRestlet() {
		this(new HashSet<String>(), new HashSet<String>());
	}


	@Override
	public final void handle(Request request, Response response) {
		// validate params
		Set<String> paramNames = request.getResourceRef().getQueryAsForm().getNames();
		for (String param : mandatoryQueryParams) {
			if (!paramNames.remove(param)) {
				onInvalidRequest(response, "missing query param " + param);
				return;
			}
		}
		paramNames.removeAll(optionalQueryParams);
		if (paramNames.size() > 0) {
			onInvalidRequest(response, "found unknown query params");
			return;
		}

		// validate method type
		Method method = request.getMethod();
		if (method.equals(Method.GET)) doGet(request, response);
		else if (method.equals(Method.POST)) doPost(request, response);
		else onInvalidMethod(response, method);
	}


	protected abstract void doGet(Request request, Response response);
	protected abstract void doPost(Request request, Response response);


	protected final void onInvalidRequest(Response response, String msg) {
		response.setStatus(Status.CLIENT_ERROR_BAD_REQUEST, msg);
	}


	protected final void onInvalidMethod(Response response, Method method) {
		onInvalidRequest(response, "method " + method.toString() + " not supported");
	}


	protected final void onSuccess(Response response) {
		response.setStatus(Status.SUCCESS_OK);
	}


	protected String getParameter(Request request, String key) {
		Parameter param = request.getResourceRef().getQueryAsForm().getFirst(key);
		if (param == null) return null;
		else return param.getValue();
	}

}
