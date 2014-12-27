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
		CouchDbConnector dataSourceConnector = couchDbInstance.createConnector(DataSourceRegistrationRepository.DATABASE_NAME, true);

		bind(CouchDbInstance.class).toInstance(couchDbInstance);
		bind(CouchDbConnector.class).annotatedWith(Names.named(DataSourceRegistrationRepository.DATABASE_NAME)).toInstance(dataSourceConnector);
	}

}
