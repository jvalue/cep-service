package org.jvalue.ceps.esper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


public final class SchemaTranslatorTest {

	@Test
	@SuppressWarnings("rawtypes")
	public final void testPegelOnline() throws Exception {
		List<EventDefinition> definitions = SchemaTranslator.toEventDefinition(
				"pegelonline",
				getJsonSchema("/schema-pegelonline.json"));

		assertNotNull(definitions);
		assertEquals(2, definitions.size());

		EventDefinition definition = definitions.get(1);
		assertEquals("pegelonline", definition.getName());
		assertTrue(definition.getSchema().containsKey("longname"));
		assertEquals(definition.getSchema().get("longname"), String.class);
		assertTrue(definition.getSchema().containsKey("shortname"));
		assertTrue(definition.getSchema().containsKey("number"));
		assertTrue(definition.getSchema().containsKey("uuid"));
		assertTrue(definition.getSchema().containsKey("km"));
		assertTrue(definition.getSchema().containsKey("agency"));
		assertTrue(((Map) definition.getSchema().get("BodyOfWater")).containsKey("longname"));
		assertTrue(((Map) definition.getSchema().get("BodyOfWater")).containsKey("shortname"));
		assertEquals(((Map) definition.getSchema().get("BodyOfWater")).get("longname"), String.class);
	}


	@Test
	public final void testPegelPortalMv() throws Exception {
		List<EventDefinition> definitions = SchemaTranslator.toEventDefinition(
				"pegelportalMv",
				getJsonSchema("/schema-pegelportal-mv.json"));

		assertNotNull(definitions);
		assertEquals(1, definitions.size());

		EventDefinition definition = definitions.get(0);
		assertEquals("pegelportalMv", definition.getName());
		assertTrue(definition.getSchema().containsKey("timestamp"));
		assertTrue(definition.getSchema().containsKey("water"));
		assertTrue(definition.getSchema().containsKey("level"));
	}


	@Test
	public final void testOsm() throws Exception {
		List<EventDefinition> definitions = SchemaTranslator.toEventDefinition(
				"osm",
				getJsonSchema("/schema-osm.json"));

		assertNotNull(definitions);
		assertEquals(1, definitions.size());

		EventDefinition definition = definitions.get(0);
		assertEquals("osm", definition.getName());
		assertTrue(definition.getSchema().containsKey("longitude"));
		assertTrue(definition.getSchema().containsKey("latitude"));
	}


	private static final ObjectMapper mapper = new ObjectMapper();

	private JsonNode getJsonSchema(String resourceName) throws Exception {
		URL schemaUrl = getClass().getResource(resourceName);
		BufferedReader reader = new BufferedReader(new FileReader(new File(schemaUrl.toURI())));
		StringBuilder builder = new StringBuilder();
		String line;
		while ((line = reader.readLine()) != null) builder.append(line);
		return mapper.readTree(builder.toString());
	}

}
