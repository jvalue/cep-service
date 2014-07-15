package org.jvalue.ceps.rest.restlet;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.jvalue.ceps.notifications.NotificationManager;
import org.jvalue.ceps.notifications.clients.Client;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.MediaType;
import org.restlet.data.Method;


abstract class BaseNotificationRegisterRestlet extends BaseRestlet {

	private static final String PARAM_EPL_STMT = "eplStmt";
	protected static final Set<String> BASE_PARAMS;
	static {
		Set<String> params = new HashSet<String>();
		params.add(PARAM_EPL_STMT);
		BASE_PARAMS = Collections.unmodifiableSet(params);
	}


	protected BaseNotificationRegisterRestlet(
			Set<String> mandatoryQueryParams,
			Set<String> optionalQueryParams) {
		
		super(mandatoryQueryParams, optionalQueryParams);
	}


	@Override
	protected final void doGet(Request request, Response response) {
		onInvalidMethod(response, Method.GET);
	}


	@Override
	protected final void doPost(Request request, Response response) {
		String eplStmt = getParameter(request, PARAM_EPL_STMT);
		Client client = getClient(request, eplStmt);
		NotificationManager.getInstance().register(client);
		response.setEntity(client.getClientId(), MediaType.TEXT_PLAIN);
		onSuccess(response);
	}


	protected abstract Client getClient(Request request, String eplStmt);

}
