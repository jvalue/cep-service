package org.jvalue.ceps.esper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


public final class DataTranslatorTest {


	private static final ObjectMapper mapper = new ObjectMapper();

	@Test
	@SuppressWarnings("rawtypes")
	public void testToMap() throws Exception {

		URL jsonUrl = getClass().getResource("/data-pegelonline1.json");
		JsonNode json = mapper.readTree(new File(jsonUrl.toURI())); 
		Map<String, Object> map = new DataTranslator().toMap(json.get(0));

		assertNotNull(map);
		assertTrue(map.containsKey("_id"));
		assertTrue(map.containsKey("_rev"));
		assertTrue(map.containsKey("coordinate"));
		assertTrue(((Map) map.get("coordinate")).containsKey("longitude"));
		assertEquals(((Map) ((Object[]) map.get("timeseries"))[0]).get("shortname"), "W");

	}

}
