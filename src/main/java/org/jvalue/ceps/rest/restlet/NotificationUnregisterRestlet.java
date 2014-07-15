package org.jvalue.ceps.rest.restlet;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.jvalue.ceps.notifications.NotificationManager;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Method;


final class NotificationUnregisterRestlet extends BaseRestlet {

	private static final String PARAM_CLIENT_ID = "clientId";
	private static final Set<String> PARAMS;
	static {
		Set<String> params = new HashSet<String>();
		params.add(PARAM_CLIENT_ID);
		PARAMS = Collections.unmodifiableSet(params);
	}


	protected NotificationUnregisterRestlet() {
		super(PARAMS, new HashSet<String>());
	}


	@Override
	protected final void doGet(Request request, Response response) {
		onInvalidMethod(response, Method.GET);
	}


	@Override
	protected final void doPost(Request request, Response response) {
		String clientId = getParameter(request, PARAM_CLIENT_ID);
		NotificationManager.getInstance().unregister(clientId);
		onSuccess(response);
	}

}
