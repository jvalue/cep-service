package org.jvalue.ceps.esper;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.jvalue.ceps.utils.Log;

import com.fasterxml.jackson.databind.JsonNode;


final class SchemaTranslator {

	private SchemaTranslator() { }


	private static final String
		KEY_ATTRIBUTES = "attributes",
		KEY_NAME = "name",
		KEY_REFERENCED_OBJECTS = "referencedObjects";


	public static List<EventDefinition> toEventDefinition(
			String objectName,
			JsonNode jsonObjectSchema) {

		List<EventDefinition> allObjects = new LinkedList<EventDefinition>();
		Map<String, Object> objectSchema = readSchema(objectName, jsonObjectSchema, allObjects);
		allObjects.add(new EventDefinition(objectName, objectSchema));

		return allObjects;
	}


	private static Map<String, Object> readSchema(
			String objectName,
			JsonNode jsonObjectSchema,
			List<EventDefinition> allObjects) {

		Map<String, Object> objectSchema = new HashMap<String, Object>();

		JsonNode attributes = jsonObjectSchema.get(KEY_ATTRIBUTES);
		readAttributes(objectName, objectSchema, attributes);

		JsonNode references = jsonObjectSchema.get(KEY_REFERENCED_OBJECTS);
		readReferenceObjects(objectName, objectSchema, references, allObjects);

		return objectSchema;
	}


	private static void readAttributes(
			String objectName,
			Map<String, Object> objectSchema, 
			JsonNode objectAttributes) {

		if (objectAttributes.size() == 0) return;

		if (objectAttributes.isArray()) {

			JsonNode attribute = objectAttributes.get(0);
			String attributeType = toObjectType(attribute.get(KEY_NAME).asText(), true);
			objectSchema.put(objectName, attributeType);

		} else if (objectAttributes.isObject()) {

			Iterator<Map.Entry<String, JsonNode>> iter = objectAttributes.fields();
			while (iter.hasNext()) {
				Map.Entry<String, JsonNode> attribute = iter.next();
				String attributeName = attribute.getKey();
				String attributeType = toObjectType(
						attribute.getValue().get(KEY_NAME).asText(), 
						false);
				objectSchema.put(attributeName, attributeType);
			}

		} else Log.error("failed to read attributes (" + objectAttributes.toString() + ")");


	}


	private static void readReferenceObjects(
			String objectName,
			Map<String, Object> objectSchema, 
			JsonNode objectReferences,
			List<EventDefinition> allObjects) {

		if (objectReferences.size() == 0) return;

		if (objectReferences.isArray()) {

			JsonNode reference = objectReferences.get(0);
			String referenceType = objectName + UUID.randomUUID().toString();
			String referenceName = objectName;
			Map<String, Object> referenceSchema = readSchema(
					referenceName,
					reference,
					allObjects);

			allObjects.add(new EventDefinition(referenceType, referenceSchema));
			objectSchema.put(referenceName, referenceType + "[]");

		} else if (objectReferences.isObject()) {

			Iterator<Map.Entry<String, JsonNode>> iter = objectReferences.fields();
			while (iter.hasNext()) {

				Map.Entry<String, JsonNode> reference = iter.next();
				String referenceName = reference.getKey();
				Map<String, Object> referenceSchema = readSchema(
						referenceName,
						reference.getValue(),
						allObjects);

				objectSchema.put(referenceName, referenceSchema);
			}


		} else Log.error("found invalid schema (" + objectReferences.toString() + ")");

	}


	private static String toObjectType(String jsonObjectType, boolean isArray) {
		String type = null;
		if (jsonObjectType.equals("java.lang.String")) type = "String";
		else if (jsonObjectType.equals("java.lang.Number")) type = "double";
		else if (jsonObjectType.equals("Null")) type = "Null";
		else Log.error("failed to parse object type " + jsonObjectType);

		if (isArray) type = type + "[]";
		return type;
	}

}
