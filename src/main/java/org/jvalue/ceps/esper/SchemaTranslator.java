package org.jvalue.ceps.esper;

import com.fasterxml.jackson.databind.JsonNode;

import org.jvalue.ceps.utils.Log;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;


final class SchemaTranslator {

	// TODO this probably does not work anymore with the new ODS version
	private static final String
			KEY_ATTRIBUTES = "attributes",
			KEY_NAME = "name",
			KEY_REFERENCED_OBJECTS = "referencedObjects";


	SchemaTranslator() { }


	public List<EventDefinition> toEventDefinition(
			String objectName,
			JsonNode jsonObjectSchema) {

		List<EventDefinition> allObjects = new LinkedList<EventDefinition>();
		Map<String, Object> objectSchema = readSchema(objectName, jsonObjectSchema, allObjects);
		allObjects.add(new EventDefinition(objectName, objectSchema));

		return allObjects;
	}


	private Map<String, Object> readSchema(
			String objectName,
			JsonNode jsonObjectSchema,
			List<EventDefinition> allObjects) {

		Map<String, Object> objectSchema = new HashMap<String, Object>();

		JsonNode attributes = jsonObjectSchema.get(KEY_ATTRIBUTES);
		readAttributes(objectName, objectSchema, attributes);

		JsonNode references = jsonObjectSchema.get(KEY_REFERENCED_OBJECTS);
		readReferenceObjects(objectSchema, references, allObjects);

		return objectSchema;
	}


	private void readAttributes(
			String objectName,
			Map<String, Object> objectSchema, 
			JsonNode objectAttributes) {

		if (objectAttributes.size() == 0) return;

		if (objectAttributes.isArray()) {

			JsonNode attribute = objectAttributes.get(0);
			Object attributeType = toObjectType(attribute.get(KEY_NAME).asText(), true);
			objectSchema.put(objectName, attributeType);

		} else if (objectAttributes.isObject()) {

			Iterator<Map.Entry<String, JsonNode>> iter = objectAttributes.fields();
			while (iter.hasNext()) {
				Map.Entry<String, JsonNode> attribute = iter.next();
				String attributeName = attribute.getKey();
				Object attributeType = toObjectType(
						attribute.getValue().get(KEY_NAME).asText(), 
						false);
				objectSchema.put(attributeName, attributeType);
			}

		} else Log.error("failed to read attributes (" + objectAttributes.toString() + ")");
	}


	private void readReferenceObjects(
			Map<String, Object> objectSchema, 
			JsonNode objectReferences,
			List<EventDefinition> allObjects) {

		if (objectReferences.size() == 0) return;

		if (objectReferences.isObject()) {

			Iterator<Map.Entry<String, JsonNode>> iter = objectReferences.fields();
			while (iter.hasNext()) {

				Map.Entry<String, JsonNode> reference = iter.next();
				String referenceName = reference.getKey();
				JsonNode referenceValue = reference.getValue();

				// check for reference array
				if (!referenceValue.get(KEY_REFERENCED_OBJECTS).isArray()) {
					Map<String, Object> referenceSchema = readSchema(
							referenceName,
							referenceValue,
							allObjects);
					objectSchema.put(referenceName, referenceSchema);

				} else {
					if (referenceValue.get(KEY_REFERENCED_OBJECTS).size() == 0) continue;

					String referenceType = referenceName + UUID.randomUUID().toString();
					objectSchema.put(referenceName, referenceType + "[]");

					Map<String, Object> referenceSchema = readSchema(
							referenceName,
							referenceValue.get(KEY_REFERENCED_OBJECTS).get(0),
							allObjects);
					allObjects.add(new EventDefinition(referenceType, referenceSchema));
				}

			}


		} else Log.error("found invalid schema (" + objectReferences.toString() + ")");

	}


	@SuppressWarnings("rawtypes")
	private Object toObjectType(String jsonObjectType, boolean isArray) {
		Object type = null;
		if (jsonObjectType.equals("java.lang.String")) type = String.class;
		else if (jsonObjectType.equals("java.lang.Number")) type = double.class;
		else if (jsonObjectType.equals("Null")) type = "Null";
		else Log.error("failed to parse object type " + jsonObjectType);

		if (isArray) {
			Class<?> classType = (Class) type;
			try {
				type = Class.forName("[L" + classType.getName() + ";");
			} catch (ClassNotFoundException cne) {
				Log.error("failed to get object type", cne);
			}
		}

		return type;
	}

}
