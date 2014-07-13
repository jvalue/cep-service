package org.jvalue.ceps.data;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.jvalue.ceps.db.DummyDbAccessor;

public final class DataSourceDbTest {

	@Test
	public void testCrud() throws Exception {

		DataSource[] sources = {
			new DataSource("dummy", "dummy", "dummy"),
			new DataSource("dummy1", "dummy", "dummy"),
			new DataSource("dummy2", "dummy", "dummy")
		};

		DataSourceDb db = new DataSourceDb(new DummyDbAccessor());

		for (DataSource source : sources) {
			assertFalse(db.getAll().contains(source));
			db.add(source);
			assertTrue(db.getAll().contains(source));
		}

		for (DataSource source : sources) {
			db.remove(source);
			assertFalse(db.getAll().contains(source));
		}
	}

}
