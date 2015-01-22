package org.jvalue.ceps.db;


import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbInstance;

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
					.username(couchDbConfig.getUsername())
					.password(couchDbConfig.getPassword())
					.build());
			bind(CouchDbInstance.class).toInstance(couchDbInstance);

			CouchDbConnector dataSourceConnector = couchDbInstance.createConnector(OdsRegistrationRepository.DATABASE_NAME, true);
			bind(CouchDbConnector.class).annotatedWith(Names.named(OdsRegistrationRepository.DATABASE_NAME)).toInstance(dataSourceConnector);

			CouchDbConnector eventConnector = couchDbInstance.createConnector(EventRepository.DATABASE_NAME, true);
			bind(CouchDbConnector.class).annotatedWith(Names.named(EventRepository.DATABASE_NAME)).toInstance(eventConnector);

			CouchDbConnector clientConnector = couchDbInstance.createConnector(ClientRepository.DATABASE_NAME, true);
			bind(CouchDbConnector.class).annotatedWith(Names.named(ClientRepository.DATABASE_NAME)).toInstance(clientConnector);
		} catch (MalformedURLException mue) {
			throw new RuntimeException(mue);
		}
	}

}
