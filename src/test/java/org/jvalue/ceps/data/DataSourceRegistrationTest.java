package org.jvalue.ceps.data;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


public final class DataSourceRegistrationTest {

	private static final ObjectMapper mapper = new ObjectMapper();


	@Test
	public void testGet() {
		DataSource source = new DataSource("dummy1", "dummy2", "dummy3");
		DataSourceRegistration reg = new DataSourceRegistration("dummy", source);
		assertEquals("dummy", reg.getClientId());
		assertEquals(source, reg.getDataSource());
	}


	@Test
	public void testEqualsAndHashCode() {
		DataSource source1 = new DataSource("dummy1", "dummy2", "dummy3");
		DataSource source2 = new DataSource("dummy4", "dummy5", "dummy6");
		DataSourceRegistration reg1 = new DataSourceRegistration("dummy", source1);
		DataSourceRegistration reg2 = new DataSourceRegistration("dummy", source1);
		DataSourceRegistration reg3 = new DataSourceRegistration("dummy2", source1);
		DataSourceRegistration reg4 = new DataSourceRegistration("dummy", source2);
		DataSourceRegistration reg5 = new DataSourceRegistration("dummy2", source2);

		assertEquals(reg1, reg2);
		assertNotEquals(reg1, reg3);
		assertNotEquals(reg1, reg4);
		assertNotEquals(reg1, reg5);

		assertEquals(reg1.hashCode(), reg2.hashCode());
		assertNotEquals(reg1.hashCode(), reg3.hashCode());
		assertNotEquals(reg1.hashCode(), reg4.hashCode());
		assertNotEquals(reg1.hashCode(), reg5.hashCode());
	}


	@Test
	public void testJson() throws JsonProcessingException {
		DataSource source = new DataSource("dummy1", "dummy2", "dummy3");
		DataSourceRegistration reg = new DataSourceRegistration("dummy", source);
		JsonNode json = mapper.valueToTree(reg);
		assertNotNull(json);
		assertEquals(reg, mapper.treeToValue(json, DataSourceRegistration.class));
	}

}
