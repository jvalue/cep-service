package org.jvalue.ceps.rest.notifications;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jvalue.ceps.adapter.ClientAdapter;
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
			ClientAdapter adapter) {
		Set<String> result = new HashSet<String>(subclassParams);
		result.addAll(adapter.getRequiredParams());
		result.add(PARAM_DEVICE_ID);
		return result;
	}


	private final NotificationManager manager;
	private final ClientAdapter clientAdapter;

	protected BaseRegisterRestlet(
			NotificationManager manager,
			ClientAdapter clientAdapter,
			Set<String> mandatoryQueryParams) {
		
		super(prepareRequiredParams(mandatoryQueryParams, clientAdapter), false);
		Assert.assertNotNull(manager, clientAdapter);
		this.manager = manager;
		this.clientAdapter = clientAdapter;
	}


	@Override
	protected final RestletResult doPost(Request request) {
		String deviceId = getParameter(request, PARAM_DEVICE_ID);

		// build client
		Map<String, String> eplParams = new HashMap<String, String>();
		for (String paramKey : clientAdapter.getRequiredParams()) {
			eplParams.put(paramKey, getParameter(request, paramKey));
		}
		Client client = getClient(request, deviceId, clientAdapter.toEplStmt(eplParams));
		manager.register(client);

		Map<String, Object> resultData = new HashMap<String, Object>();
		resultData.put(KEY_CLIENT_ID, client.getClientId());

		return RestletResult.newSuccessResult(mapper.valueToTree(resultData));
	}


	protected abstract Client getClient(Request request, String deviceId, String eplStmt);

}
