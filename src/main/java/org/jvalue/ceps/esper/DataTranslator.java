package org.jvalue.ceps.esper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jvalue.ceps.utils.Assert;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


final class DataTranslator {

	private DataTranslator() { }


	private static final ObjectMapper mapper = new ObjectMapper();
	private static final TypeReference<HashMap<String,Object>> typeReference 
		= new TypeReference<HashMap<String,Object>>() {};

	public static Map<String, Object> toMap(JsonNode data) throws IOException {
		Assert.assertNotNull(data);
		return mapper.readValue(data.toString(), typeReference);
	}

}
