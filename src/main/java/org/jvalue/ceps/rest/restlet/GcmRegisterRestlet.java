package org.jvalue.ceps.rest.restlet;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.jvalue.ceps.notifications.clients.Client;
import org.jvalue.ceps.notifications.clients.ClientFactory;
import org.restlet.Request;


final class GcmRegisterRestlet extends BaseNotificationRegisterRestlet {

	private static final String PARAM_EPL_STMT = "eplStmt";
	private static final Set<String> GCM_PARAMS;
	static {
		Set<String> params = new HashSet<String>();
		params.addAll(BaseNotificationRegisterRestlet.BASE_PARAMS);
		params.add(PARAM_EPL_STMT);
		GCM_PARAMS = Collections.unmodifiableSet(params);
	}


	protected GcmRegisterRestlet() {
		super(GCM_PARAMS, new HashSet<String>());
	}


	@Override
	protected Client getClient(Request request, String eplStmt) {
		String gcmId = getParameter(request, PARAM_EPL_STMT);
		return ClientFactory.createGcmClient(eplStmt, gcmId);
	}

}
