package org.jvalue.ceps.db;

import org.jvalue.ceps.utils.Assert;


public final class DbAccessorFactory {

	private DbAccessorFactory() { }


	public static DbAccessor getCouchDbAccessor(String dbName) {
		Assert.assertNotNull(dbName);
		return new CouchDbAccessor(dbName);
	}

}
