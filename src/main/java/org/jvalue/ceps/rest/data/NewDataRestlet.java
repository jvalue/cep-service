package org.jvalue.ceps.rest.data;

import java.net.URLDecoder;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.jvalue.ceps.data.DataManager;
import org.jvalue.ceps.rest.BaseRestlet;
import org.jvalue.ceps.rest.RestletResult;
import org.jvalue.ceps.utils.Assert;
import org.jvalue.ceps.utils.Log;
import org.restlet.Request;
import org.restlet.data.Status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


final class NewDataRestlet extends BaseRestlet {

	private static final ObjectMapper mapper = new ObjectMapper();

	private static final Set<String> PARAMS;
	static {
		Set<String> params = new HashSet<String>();
		params.add(OdsRestHook.PARAM_SOURCE);
		PARAMS = Collections.unmodifiableSet(params);
	}


	private final DataManager manager;

	public NewDataRestlet(DataManager manager) {
		super(PARAMS, false);
		Assert.assertNotNull(manager);
		this.manager = manager;
	}


	@Override
	protected RestletResult doPost(Request request) {
		try {
			String sourceId = getParameter(request, OdsRestHook.PARAM_SOURCE);
			String rawString = request.getEntity().getText();

			String jsonString = URLDecoder.decode(rawString, "UTF-8");
			JsonNode data = mapper.readTree(jsonString);

			manager.onSourceChanged(sourceId, data);

			return RestletResult.newSuccessResult();

		} catch (Exception e) {
			Log.error("retreiving data from ods failed", e);
			return RestletResult.newErrorResult(
					Status.SERVER_ERROR_INTERNAL,
					"retreiving data from ods failed");
		}
	}

}
