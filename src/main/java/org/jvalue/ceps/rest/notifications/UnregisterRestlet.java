package org.jvalue.ceps.rest.notifications;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.jvalue.ceps.notifications.NotificationManager;
import org.jvalue.ceps.rest.BaseRestlet;
import org.jvalue.ceps.utils.Assert;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Method;


final class UnregisterRestlet extends BaseRestlet {

	private static final String PARAM_CLIENT_ID = "clientId";
	private static final Set<String> PARAMS;
	static {
		Set<String> params = new HashSet<String>();
		params.add(PARAM_CLIENT_ID);
		PARAMS = Collections.unmodifiableSet(params);
	}

	
	private final NotificationManager manager;

	protected UnregisterRestlet(NotificationManager manager) {
		super(PARAMS, new HashSet<String>());
		Assert.assertNotNull(manager);
		this.manager = manager;
	}


	@Override
	protected final void doGet(Request request, Response response) {
		onInvalidMethod(response, Method.GET);
	}


	@Override
	protected final void doPost(Request request, Response response) {
		String clientId = getParameter(request, PARAM_CLIENT_ID);
		manager.unregister(clientId);
		onSuccess(response);
	}

}
