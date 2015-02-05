package org.jvalue.ceps.api.adapter;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public final class ArgumentTypeSerializer extends JsonSerializer<ArgumentType> {

	@Override
	public void serialize(ArgumentType value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
		gen.writeString(value.name());
	}
}
