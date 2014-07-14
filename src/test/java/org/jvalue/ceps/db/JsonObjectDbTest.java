package org.jvalue.ceps.db;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class JsonObjectDbTest {

	@Test
	public void testCrud() throws Exception {

		Item[] items = {
			new Item("dummy1"),
			new Item("dummy2"),
			new Item("dummy3")
		};


		JsonObjectDb<Item> db = new JsonObjectDb<Item>(new DummyDbAccessor(), Item.class);

		for (Item item : items) {
			assertFalse(db.getAll().contains(item));
			db.add(item);
			assertTrue(db.getAll().contains(item));
		}

		for (Item item : items) {
			db.remove(item);
			assertFalse(db.getAll().contains(item));
		}
	}


	@JsonIgnoreProperties(ignoreUnknown = true)
	static final class Item {

		private final String value;

		@JsonCreator
		public Item(
				@JsonProperty("value") String value) {
			
			assertNotNull(value);
			this.value = value;
		}

		public String getValue() {
			return value;
		}

		@Override
		public boolean equals(Object other) {
			if (other == null || !(other instanceof Item)) return false;
			Item item = (Item) other;
			return value.equals(item.value);
		}

		@Override
		public int hashCode() {
			return 13 + 17 * value.hashCode();
		}

	}

}
