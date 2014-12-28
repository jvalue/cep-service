package org.jvalue.ceps.db;


import com.google.inject.Inject;
import com.google.inject.name.Named;

import org.ektorp.CouchDbConnector;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.View;
import org.jvalue.ceps.data.OdsRegistration;

@View( name = "all", map = "function(doc) { if (doc.clientId && doc.dataSource ) emit(null, doc)}")
public final class OdsRegistrationRepository extends CouchDbRepositorySupport<OdsRegistration> {

	static final String DATABASE_NAME = "sources";

	@Inject
	OdsRegistrationRepository(@Named(DATABASE_NAME) CouchDbConnector connector) {
		super(OdsRegistration.class, connector);
		initStandardDesignDocument();
	}

}
