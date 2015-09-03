package org.jvalue.ceps.db;


import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbInstance;
import org.jvalue.commons.auth.UserRepository;
import org.jvalue.commons.couchdb.CouchDbConfig;
import org.jvalue.commons.couchdb.DbConnectorFactory;

import java.net.MalformedURLException;

public class DbModule extends AbstractModule {

	private final CouchDbConfig couchDbConfig;

	public DbModule(CouchDbConfig couchDbConfig) {
		this.couchDbConfig = couchDbConfig;
	}


	@Override
	protected void configure() {
		try {
			CouchDbInstance couchDbInstance = new StdCouchDbInstance(new StdHttpClient.Builder()
					.url(couchDbConfig.getUrl())
					.username(couchDbConfig.getAdmin().getUsername())
					.password(couchDbConfig.getAdmin().getPassword())
					.maxConnections(couchDbConfig.getMaxConnections())
					.build());
			DbConnectorFactory connectorFactory = new DbConnectorFactory(couchDbInstance, couchDbConfig.getDbPrefix());

			CouchDbConnector dataSourceConnector = connectorFactory.createConnector(OdsRegistrationRepository.DATABASE_NAME, true);
			bind(CouchDbConnector.class).annotatedWith(Names.named(OdsRegistrationRepository.DATABASE_NAME)).toInstance(dataSourceConnector);

			CouchDbConnector clientConnector = connectorFactory.createConnector(ClientRepository.DATABASE_NAME, true);
			bind(CouchDbConnector.class).annotatedWith(Names.named(ClientRepository.DATABASE_NAME)).toInstance(clientConnector);

			CouchDbConnector eplAdapterConnector = connectorFactory.createConnector(EplAdapterRepository.DATABASE_NAME, true);
			bind(CouchDbConnector.class).annotatedWith(Names.named(EplAdapterRepository.DATABASE_NAME)).toInstance(eplAdapterConnector);
			
			CouchDbConnector userConnector = connectorFactory.createConnector(UserRepository.DATABASE_NAME, true);
			bind(CouchDbConnector.class).annotatedWith(Names.named(UserRepository.DATABASE_NAME)).toInstance(userConnector);

		} catch (MalformedURLException mue) {
			throw new RuntimeException(mue);
		}
	}

}
