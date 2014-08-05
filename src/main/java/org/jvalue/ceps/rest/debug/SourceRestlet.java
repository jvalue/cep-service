package org.jvalue.ceps.rest.debug;

import java.util.Set;

import org.jvalue.ceps.data.DataManager;
import org.jvalue.ceps.data.DataSourceRegistration;
import org.jvalue.ceps.rest.BaseRestlet;
import org.jvalue.ceps.rest.RestletResult;
import org.jvalue.ceps.utils.Assert;
import org.restlet.Request;

import com.fasterxml.jackson.databind.ObjectMapper;


final class SourceRestlet extends BaseRestlet {

	private static final ObjectMapper mapper = new ObjectMapper();

	private final DataManager manager;

	protected SourceRestlet(DataManager manager) {
		Assert.assertNotNull(manager);
		this.manager = manager;
	}


	@Override
	protected final RestletResult doGet(Request request) {
		Set<DataSourceRegistration> clients = manager.getAll();
		return RestletResult.newSuccessResult(mapper.valueToTree(clients));
	}

}
