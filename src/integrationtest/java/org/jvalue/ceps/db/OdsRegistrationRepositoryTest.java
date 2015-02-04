package org.jvalue.ceps.db;


import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.ektorp.CouchDbInstance;
import org.jvalue.ceps.data.OdsRegistration;
import org.jvalue.common.db.RepositoryAdapter;
import org.jvalue.ods.api.notifications.HttpClient;
import org.jvalue.ods.api.sources.DataSource;

public final class OdsRegistrationRepositoryTest extends AbstractRepositoryAdapterTest<OdsRegistration> {

	public OdsRegistrationRepositoryTest() {
		super(OdsRegistrationRepositoryTest.class.getSimpleName());
	}


	@Override
	protected RepositoryAdapter<?, ?, OdsRegistration> doCreateAdapter(CouchDbInstance couchDbInstance, String databaseName) {
		return new OdsRegistrationRepository(couchDbInstance.createConnector(databaseName, true));
	}


	@Override
	protected OdsRegistration doCreateValue(String id, String data) {
		return new OdsRegistration(
				id,
				new DataSource(id, null, new ObjectNode(JsonNodeFactory.instance), null),
				new HttpClient(id, data, false));
	}

}
