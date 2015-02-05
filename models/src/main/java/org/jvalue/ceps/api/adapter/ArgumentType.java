package org.jvalue.ceps.api.adapter;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonDeserialize(using = ArgumentTypeDeserializer.class)
@JsonSerialize(using = ArgumentTypeSerializer.class)
public enum ArgumentType {

	STRING(String.class),
	NUMBER(Double.class),
	BOOLEAN(Boolean.class);

	private final Class<?> javaType;

	ArgumentType(Class<?> javaType) {
		this.javaType = javaType;
	}

	public Class<?> getJavaType() {
		return javaType;
	}
}
