package org.jvalue.ceps.api.adapter;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

import java.util.Map;

import javax.validation.constraints.NotNull;


/**
 * Describes one adapter that is capable of transforming client parameters into
 * a valid EPL statement.
 */
public final class EplAdapter {

	@NotNull private final String id;
	@NotNull private final String eplBlueprint;
	@NotNull private final Map<String, ArgumentType> requiredArguments;


	public EplAdapter(
			@JsonProperty("id") String id,
			@JsonProperty("eplBlueprint") String eplBlueprint,
			@JsonProperty("requiredArguments") Map<String, ArgumentType> requiredArguments) {

		this.id = id;
		this.eplBlueprint = eplBlueprint;
		this.requiredArguments = requiredArguments;
	}


	public String getId() {
		return id;
	}


	public String getEplBlueprint() {
		return eplBlueprint;
	}


	public Map<String, ArgumentType> getRequiredArguments() {
		return requiredArguments;
	}


	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof EplAdapter)) return false;
		if (other == this) return true;
		EplAdapter adapter = (EplAdapter) other;
		return Objects.equal(id, adapter.id)
				&& Objects.equal(eplBlueprint, adapter.eplBlueprint)
				&& Objects.equal(requiredArguments, adapter.requiredArguments);
	}


	@Override
	public int hashCode() {
		return Objects.hashCode(id, eplBlueprint, requiredArguments);
	}

}
