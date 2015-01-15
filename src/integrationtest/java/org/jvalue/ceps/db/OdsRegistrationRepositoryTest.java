package org.jvalue.ceps.db;


import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.ektorp.CouchDbInstance;
import org.jvalue.ceps.data.OdsRegistration;
import org.jvalue.ods.api.notifications.HttpClient;
import org.jvalue.ods.api.sources.DataSource;
import org.jvalue.ods.api.sources.DataSourceMetaData;

import java.util.HashMap;
import java.util.Map;

public final class OdsRegistrationRepositoryTest extends AbstractRepositoryTest<OdsRegistrationRepository, OdsRegistration> {

	private static final String
			SOURCE_ID_1 = "sourceId1",
			SOURCE_ID_2 = "sourceId2";


	public OdsRegistrationRepositoryTest() {
		super(OdsRegistrationRepositoryTest.class.getSimpleName());
	}


	@Override
	protected OdsRegistrationRepository doCreateDatabase(CouchDbInstance couchDbInstance, String databaseName) {
		return new OdsRegistrationRepository(couchDbInstance.createConnector(databaseName, true));
	}


	@Override
	protected Map<String, OdsRegistration> doSetupDataItems() {
		Map<String, OdsRegistration> registrations = new HashMap<>();
		registrations.put(SOURCE_ID_1, createRegistration(SOURCE_ID_1));
		registrations.put(SOURCE_ID_2, createRegistration(SOURCE_ID_2));
		return registrations;
	}


	@Override
	protected String doGetIdForItem(OdsRegistration registration) {
		return registration.getDataSource().getId();
	}


	private OdsRegistration createRegistration(String sourceId) {
		DataSource source = new DataSource(
				sourceId,
				JsonPointer.compile("/someDomainKey"),
				new ObjectNode(JsonNodeFactory.instance),
				new DataSourceMetaData("", "", "", "", "", "", ""));
		HttpClient client = new HttpClient("someClientId", "someCallbackUrl", true);
		return new OdsRegistration(source, client);
	}

}
