package org.jvalue.ceps.api.adapter;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public final class ArgumentTypeDeserializer extends JsonDeserializer<ArgumentType> {

	@Override
	public ArgumentType deserialize(JsonParser parser, DeserializationContext context) throws IOException {
		try {
			return ArgumentType.valueOf(parser.getValueAsString());
		} catch (IllegalArgumentException iae) {
			throw new IOException(iae);
		}
	}

}
