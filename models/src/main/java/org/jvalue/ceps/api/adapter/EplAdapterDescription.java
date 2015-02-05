package org.jvalue.ceps.api.adapter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;


public final class EplAdapterDescription extends AbstractEplAdapter {

	@JsonCreator
	public EplAdapterDescription(
			@JsonProperty("eplBlueprint") String eplBlueprint,
			@JsonProperty("requiredArguments") Map<String, ArgumentType> requiredArguments) {

		super(eplBlueprint, requiredArguments);
	}

}
