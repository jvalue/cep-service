package org.jvalue.ceps.api.adapter;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.TextNode;

import java.io.IOException;

public final class ArgumentTypeDeserializer extends JsonDeserializer<ArgumentType> {

	@Override
	public ArgumentType deserialize(JsonParser parser, DeserializationContext context) throws IOException, JsonProcessingException {
		TextNode node = parser.getCodec().readTree(parser);
		try {
			return ArgumentType.valueOf(node.asText());
		} catch (IllegalArgumentException iae) {
			throw new IOException(iae);
		}
	}

}
