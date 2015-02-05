package org.jvalue.ceps.api.adapter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

import java.util.Map;

import javax.validation.constraints.NotNull;


public final class EplAdapter extends AbstractEplAdapter {

	@NotNull private final String id;

	@JsonCreator
	public EplAdapter(
			@JsonProperty("id") String id,
			@JsonProperty("eplBlueprint") String eplBlueprint,
			@JsonProperty("requiredArguments") Map<String, ArgumentType> requiredArguments) {

		super(eplBlueprint, requiredArguments);
		this.id = id;
	}


	public String getId() {
		return id;
	}


	@Override
	public boolean equals(Object other) {
		if (!super.equals(other) || !(other instanceof EplAdapter)) return false;
		EplAdapter adapter = (EplAdapter) other;
		return Objects.equal(id, adapter.id);
	}


	@Override
	public int hashCode() {
		return Objects.hashCode(super.hashCode(), id);
	}

}
