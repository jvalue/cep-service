package org.jvalue.ceps.rest.notifications;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.jvalue.ceps.notifications.NotificationManager;
import org.jvalue.ceps.rest.BaseRestlet;
import org.jvalue.ceps.rest.RestletResult;
import org.jvalue.ceps.utils.Assert;
import org.restlet.Request;


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
		super(PARAMS, false);
		Assert.assertNotNull(manager);
		this.manager = manager;
	}


	@Override
	protected final RestletResult doPost(Request request) {
		String clientId = getParameter(request, PARAM_CLIENT_ID);
		manager.unregister(clientId);
		return RestletResult.newSuccessResult();
	}

}
