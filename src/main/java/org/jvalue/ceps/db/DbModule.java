package org.jvalue.ceps.db;


import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbInstance;

public class DbModule extends AbstractModule {

	@Override
	protected void configure() {
		CouchDbInstance couchDbInstance = new StdCouchDbInstance(new StdHttpClient.Builder().build());
		bind(CouchDbInstance.class).toInstance(couchDbInstance);

		CouchDbConnector dataSourceConnector = couchDbInstance.createConnector(DataSourceRegistrationRepository.DATABASE_NAME, true);
		bind(CouchDbConnector.class).annotatedWith(Names.named(DataSourceRegistrationRepository.DATABASE_NAME)).toInstance(dataSourceConnector);

		CouchDbConnector eventConnector = couchDbInstance.createConnector(EventRepository.DATABASE_NAME, true);
		bind(CouchDbConnector.class).annotatedWith(Names.named(EventRepository.DATABASE_NAME)).toInstance(eventConnector);
	}

}
