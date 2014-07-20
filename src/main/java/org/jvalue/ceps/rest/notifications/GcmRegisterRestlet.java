package org.jvalue.ceps.rest.notifications;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.jvalue.ceps.notifications.NotificationManager;
import org.jvalue.ceps.notifications.clients.Client;
import org.jvalue.ceps.notifications.clients.ClientFactory;
import org.restlet.Request;


final class GcmRegisterRestlet extends BaseRegisterRestlet {

	private static final String PARAM_GCM_ID = "regId";
	private static final Set<String> GCM_PARAMS;
	static {
		Set<String> params = new HashSet<String>();
		params.addAll(BaseRegisterRestlet.BASE_PARAMS);
		params.add(PARAM_GCM_ID);
		GCM_PARAMS = Collections.unmodifiableSet(params);
	}


	protected GcmRegisterRestlet(NotificationManager manager) {
		super(manager, GCM_PARAMS, new HashSet<String>());
	}


	@Override
	protected Client getClient(Request request, String eplStmt) {
		String gcmId = getParameter(request, PARAM_GCM_ID);
		return ClientFactory.createGcmClient(eplStmt, gcmId);
	}

}
