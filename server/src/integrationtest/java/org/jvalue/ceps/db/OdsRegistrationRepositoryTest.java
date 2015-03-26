package org.jvalue.ceps.db;


import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.jvalue.ceps.api.data.OdsRegistration;
import org.jvalue.commons.couchdb.DbConnectorFactory;
import org.jvalue.commons.couchdb.RepositoryAdapter;
import org.jvalue.ods.api.notifications.HttpClient;
import org.jvalue.ods.api.sources.DataSource;

public final class OdsRegistrationRepositoryTest extends AbstractRepositoryAdapterTest<OdsRegistration> {

	@Override
	protected RepositoryAdapter<?, ?, OdsRegistration> doCreateAdapter(DbConnectorFactory connectorFactory) {
		return new OdsRegistrationRepository(connectorFactory.createConnector(getClass().getSimpleName(), true));
	}


	@Override
	protected OdsRegistration doCreateValue(String id, String data) {
		return new OdsRegistration(
				id,
				new DataSource(id, null, new ObjectNode(JsonNodeFactory.instance), null),
				new HttpClient(id, data, false));
	}

}
