package org.jvalue.ceps.db;


import com.google.inject.Inject;
import com.google.inject.name.Named;

import org.ektorp.CouchDbConnector;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.View;
import org.jvalue.ceps.data.DataSourceRegistration;

@View( name = "all", map = "function(doc) { if (doc.clientId && doc.dataSource ) emit(null, doc)}")
public final class DataSourceRegistrationRepository extends CouchDbRepositorySupport<DataSourceRegistration> {

	static final String DATABASE_NAME = "sources";

	@Inject
	DataSourceRegistrationRepository(@Named(DATABASE_NAME) CouchDbConnector connector) {
		super(DataSourceRegistration.class, connector);
		initStandardDesignDocument();
	}

}
