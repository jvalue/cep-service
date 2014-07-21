package org.jvalue.ceps.data;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class DataSourceTest {

	private static final ObjectMapper mapper = new ObjectMapper();


	@Test
	public void testGet() {
		DataSource source = new DataSource("dummy1", "dummy2", "dummy3");
		assertEquals("dummy1", source.getOdsSourceId());
		assertEquals("dummy2", source.getOdsUrl());
		assertEquals("dummy3", source.getOdsSchemaUrl());
	}


	@Test
	public void testEqualsAndHashCode() {
		DataSource source1 = new DataSource("dummy", "dummy", "dummy");
		DataSource source2 = new DataSource("dummy", "dummy", "dummy");
		DataSource source3 = new DataSource("dummy2", "dummy", "dummy");
		DataSource source4 = new DataSource("dummy", "dummy2", "dummy");
		DataSource source5 = new DataSource("dummy", "dummy", "dummy2");

		assertEquals(source1, source2);
		assertNotEquals(source1, source3);
		assertNotEquals(source1, source4);
		assertNotEquals(source1, source5);

		assertEquals(source1.hashCode(), source2.hashCode());
		assertNotEquals(source1.hashCode(), source3.hashCode());
		assertNotEquals(source1.hashCode(), source4.hashCode());
		assertNotEquals(source1.hashCode(), source5.hashCode());
	}


	@Test
	public void testJson() throws JsonProcessingException {
		DataSource source = new DataSource("dummy", "dummy", "dummy");
		JsonNode json = mapper.valueToTree(source);
		assertNotNull(json);
		assertEquals(source, mapper.treeToValue(json, DataSource.class));
	}

}
