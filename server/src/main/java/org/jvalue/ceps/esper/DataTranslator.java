package org.jvalue.ceps.esper;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;

import org.jvalue.ceps.utils.Assert;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


final class DataTranslator {

	private static final ObjectMapper mapper = new ObjectMapper();
	static {
		SimpleModule module = new SimpleModule();
		module.addDeserializer(Map.class, new DataSerializer());
		mapper.registerModule(module);
	}


	DataTranslator() { }


	@SuppressWarnings("unchecked")
	public Map<String, Object> toMap(JsonNode data) throws IOException {
		Assert.assertNotNull(data);
		return mapper.readValue(data.toString(), Map.class);
	}



	private static final class DataSerializer extends StdDeserializer<Map<String, Object>> {

		private static final long serialVersionUID = 4211;

		protected DataSerializer() {
			super(Map.class);
		}


		@Override
		@SuppressWarnings("unchecked")
		public Map<String, Object> deserialize(JsonParser parser, DeserializationContext context)  throws IOException {

			JsonNode node = parser.getCodec().readTree(parser);
			Assert.assertTrue(node.isObject(), "can only parse json objects");
			return (Map<String, Object>) deserialize(node);
		}


		private Object deserialize(JsonNode node) throws JsonProcessingException {
			// ArrayNode -> Object[]
			if (node.isArray()) {

				Object[] array = new Object[node.size()];
				for (int i = 0; i < array.length; i++) {
					array[i] = deserialize(node.get(i));
				}
				return array;

			// ObjectNode -> Map<String, Object>
			} else if (node.isObject()) {

				Map<String, Object> map = new HashMap<String, Object>();

				Iterator<String> fieldNames = node.fieldNames();
				while (fieldNames.hasNext()) {
					String name = fieldNames.next();
					JsonNode value = node.get(name);

					map.put(name, deserialize(value));
				}
				return map;

			// rest -> Object
			} else {
				return mapper.treeToValue(node, Object.class);
			}
		}

	}

}
