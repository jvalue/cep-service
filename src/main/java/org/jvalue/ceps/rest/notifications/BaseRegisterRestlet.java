package org.jvalue.ceps.rest.notifications;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jvalue.ceps.adapter.EplAdapter;
import org.jvalue.ceps.notifications.NotificationManager;
import org.jvalue.ceps.notifications.clients.Client;
import org.jvalue.ceps.rest.BaseRestlet;
import org.jvalue.ceps.rest.RestletResult;
import org.jvalue.ceps.utils.Assert;
import org.restlet.Request;

import com.fasterxml.jackson.databind.ObjectMapper;


abstract class BaseRegisterRestlet extends BaseRestlet {

	private static final ObjectMapper mapper = new ObjectMapper();

	private static final String KEY_CLIENT_ID = "clientId";
	private static final String PARAM_DEVICE_ID = "deviceId";


	private static Set<String> prepareRequiredParams(
			Set<String> subclassParams,
			EplAdapter eplAdapter) {
		Set<String> result = new HashSet<String>(subclassParams);
		result.addAll(eplAdapter.getRequiredParams());
		result.add(PARAM_DEVICE_ID);
		return result;
	}


	private final NotificationManager manager;
	private final EplAdapter eplAdapter;

	protected BaseRegisterRestlet(
			NotificationManager manager,
			EplAdapter eplAdapter,
			Set<String> mandatoryQueryParams) {
		
		super(prepareRequiredParams(mandatoryQueryParams, eplAdapter), false);
		Assert.assertNotNull(manager, eplAdapter);
		this.manager = manager;
		this.eplAdapter = eplAdapter;
	}


	@Override
	protected final RestletResult doPost(Request request) {
		String deviceId = getParameter(request, PARAM_DEVICE_ID);

		// build client
		Map<String, String> eplParams = new HashMap<String, String>();
		for (String paramKey : eplAdapter.getRequiredParams()) {
			eplParams.put(paramKey, getParameter(request, paramKey));
		}
		Client client = getClient(request, deviceId, eplAdapter.toEplStmt(eplParams));
		manager.register(client);

		Map<String, Object> resultData = new HashMap<String, Object>();
		resultData.put(KEY_CLIENT_ID, client.getClientId());

		return RestletResult.newSuccessResult(mapper.valueToTree(resultData));
	}


	protected abstract Client getClient(Request request, String deviceId, String eplStmt);

}
