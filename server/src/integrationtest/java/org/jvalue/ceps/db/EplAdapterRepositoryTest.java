package org.jvalue.ceps.db;


import org.jvalue.ceps.api.adapter.ArgumentType;
import org.jvalue.ceps.api.adapter.EplAdapter;
import org.jvalue.common.db.DbConnectorFactory;
import org.jvalue.common.db.RepositoryAdapter;

import java.util.HashMap;

public final class EplAdapterRepositoryTest extends AbstractRepositoryAdapterTest<EplAdapter> {

	@Override
	protected RepositoryAdapter<?, ?, EplAdapter> doCreateAdapter(DbConnectorFactory connectorFactory) {
		return new EplAdapterRepository(connectorFactory.createConnector(getClass().getSimpleName(), true));
	}


	@Override
	protected EplAdapter doCreateValue(String id, String data) {
		return new EplAdapter(id, data, new HashMap<String, ArgumentType>());
	}

}
