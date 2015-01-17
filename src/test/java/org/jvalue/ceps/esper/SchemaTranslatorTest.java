package org.jvalue.ceps.esper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public final class SchemaTranslatorTest {

	private static final ObjectMapper mapper = new ObjectMapper();

	private SchemaTranslator translator;

	@Before
	public void setupTranslator() {
		this.translator = new SchemaTranslator();
	}

	@Test
	@SuppressWarnings("rawtypes")
	public final void testPegelOnline() throws Exception {
		List<EventDefinition> definitions = translator.toEventDefinition(
				"pegelonline",
				getJsonSchema("/schema-pegelonline.json"));

		assertNotNull(definitions);
		assertEquals(3, definitions.size());

		EventDefinition definition = definitions.get(2);
		assertEquals("pegelonline", definition.getName());
		assertTrue(definition.getSchema().containsKey("longname"));
		assertEquals(definition.getSchema().get("longname"), String.class);
		assertTrue(definition.getSchema().containsKey("shortname"));
		assertTrue(definition.getSchema().containsKey("number"));
		assertTrue(definition.getSchema().containsKey("uuid"));
		assertTrue(definition.getSchema().containsKey("km"));
		assertTrue(definition.getSchema().containsKey("agency"));
		assertTrue(((Map) definition.getSchema().get("water")).containsKey("longname"));
		assertTrue(((Map) definition.getSchema().get("water")).containsKey("shortname"));
		assertEquals(((Map) definition.getSchema().get("water")).get("longname"), String.class);
		assertEquals(definition.getSchema().get("timeseries").getClass(), String.class);
	}


	private JsonNode getJsonSchema(String resourceName) throws Exception {
		URL schemaUrl = getClass().getResource(resourceName);
		BufferedReader reader = new BufferedReader(new FileReader(new File(schemaUrl.toURI())));
		StringBuilder builder = new StringBuilder();
		String line;
		while ((line = reader.readLine()) != null) builder.append(line);
		return mapper.readTree(builder.toString());
	}

}
