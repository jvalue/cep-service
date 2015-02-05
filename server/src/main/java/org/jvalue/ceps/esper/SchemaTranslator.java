package org.jvalue.ceps.esper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.jvalue.ceps.utils.Log;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;


final class SchemaTranslator {

	private static final String
			KEY_TYPE = "type",
			KEY_PROPERTIES = "properties",
			KEY_ITEMS = "items";

	SchemaTranslator() { }


	public List<EventDefinition> toEventDefinition(
			String objectName,
			JsonNode jsonObjectSchema) {

		List<EventDefinition> allObjects = new LinkedList<>();
		allObjects.add(new EventDefinition(
				objectName,
				readObject(jsonObjectSchema, allObjects)));
		return allObjects;
	}


	private Map<String, Object> readObject(
			JsonNode jsonObjectSchema,
			List<EventDefinition> allObjects) {

		Map<String, Object> objectSchema = new HashMap<>();

		JsonNode properties = jsonObjectSchema.get(KEY_PROPERTIES);
		Iterator<Map.Entry<String, JsonNode>> iter = properties.fields();
		while (iter.hasNext()) {
			Map.Entry<String, JsonNode> property = iter.next();
			String propertyName = property.getKey();
			String propertyType  = property.getValue().get(KEY_TYPE).asText();

			switch(propertyType) {
				case "object":
					objectSchema.put(
							propertyName,
							readObject(property.getValue(), allObjects));
					break;

				case "array":
					ObjectNode arrayItem = (ObjectNode) property.getValue().get(KEY_ITEMS);
					String arrayItemType = arrayItem.get(KEY_TYPE).asText();
					switch(arrayItemType) {
						case "object":
							propertyType = propertyName + "-" + UUID.randomUUID().toString();
							objectSchema.put(propertyName, propertyType + "[]");
							allObjects.add(new EventDefinition(
									propertyType,
									readObject(arrayItem, allObjects)));
							break;

						case "array":
							// Note: to support this move the handling of the array case to
							// a separate method which can handle recursive calls while
							// storing the dimension of the array.
							Log.error("Array in array schema not supported");
							break;

						default:
							objectSchema.put(propertyName, toObjectType(arrayItemType, true));
					}
					break;

				default:
					objectSchema.put(propertyName, toObjectType(propertyType, false));
			}
		}

		return objectSchema;
	}


	private Class<?> toObjectType(String jsonObjectType, boolean isArray) {
		Class<?> type;
		switch(jsonObjectType) {
			case "string":
				type = String.class;
				break;

			case "number":
				type = double.class;
				break;

			case "integer":
				type = int.class;
				break;

			default:
				Log.error("failed to parse object type " + jsonObjectType);
				return null;
		}

		if (isArray) {
			try {
				type = Class.forName("[L" + type.getName() + ";");
			} catch (ClassNotFoundException cne) {
				Log.error("failed to get array type for " + type.getName(), cne);
			}
		}

		return type;
	}

}
