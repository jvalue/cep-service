package org.jvalue.ceps.api.adapter;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonDeserialize(using = ArgumentTypeDeserializer.class)
@JsonSerialize(using = ArgumentTypeSerializer.class)
public enum ArgumentType {

	STRING,
	NUMBER,
	BOOLEAN

}
