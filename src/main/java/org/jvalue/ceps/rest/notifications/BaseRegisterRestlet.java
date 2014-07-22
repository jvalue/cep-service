package org.jvalue.ceps.rest.notifications;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jvalue.ceps.notifications.NotificationManager;
import org.jvalue.ceps.notifications.clients.Client;
import org.jvalue.ceps.rest.BaseRestlet;
import org.jvalue.ceps.rest.RestletResult;
import org.jvalue.ceps.utils.Assert;
import org.restlet.Request;

import com.fasterxml.jackson.databind.ObjectMapper;


abstract class BaseRegisterRestlet extends BaseRestlet {

	private static final ObjectMapper mapper = new ObjectMapper();

	private static final String PARAM_EPL_STMT = "eplStmt";
	private static final String KEY_CLIENTID = "clientId";

	protected static final Set<String> BASE_PARAMS;
	static {
		Set<String> params = new HashSet<String>();
		params.add(PARAM_EPL_STMT);
		BASE_PARAMS = Collections.unmodifiableSet(params);
	}


	protected final NotificationManager manager;

	protected BaseRegisterRestlet(
			NotificationManager manager,
			Set<String> mandatoryQueryParams) {
		
		super(mandatoryQueryParams, false);
		Assert.assertNotNull(manager);
		this.manager = manager;
	}


	@Override
	protected final RestletResult doPost(Request request) {
		String eplStmt = getParameter(request, PARAM_EPL_STMT);
		Client client = getClient(request, eplStmt);
		manager.register(client);

		Map<String, Object> resultData = new HashMap<String, Object>();
		resultData.put(KEY_CLIENTID, client.getClientId());

		return RestletResult.newSuccessResult(mapper.valueToTree(resultData));
	}


	protected abstract Client getClient(Request request, String eplStmt);

}
