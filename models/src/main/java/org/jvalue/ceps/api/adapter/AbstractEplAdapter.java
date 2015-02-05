package org.jvalue.ceps.api.adapter;

import com.google.common.base.Objects;

import java.util.Map;

import javax.validation.constraints.NotNull;


/**
 * Describes one adapter that is capable of transforming client parameters into
 * a valid EPL statement.
 */
abstract class AbstractEplAdapter {

	@NotNull private final String eplBlueprint;
	@NotNull private final Map<String, ArgumentType> requiredArguments;


	public AbstractEplAdapter(
			String eplBlueprint,
			Map<String, ArgumentType> requiredArguments) {

		this.eplBlueprint = eplBlueprint;
		this.requiredArguments = requiredArguments;
	}


	public String getEplBlueprint() {
		return eplBlueprint;
	}


	public Map<String, ArgumentType> getRequiredArguments() {
		return requiredArguments;
	}


	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof AbstractEplAdapter)) return false;
		if (other == this) return true;
		AbstractEplAdapter adapter = (AbstractEplAdapter) other;
		return Objects.equal(eplBlueprint, adapter.eplBlueprint)
				&& Objects.equal(requiredArguments, adapter.requiredArguments);
	}


	@Override
	public int hashCode() {
		return Objects.hashCode(eplBlueprint, requiredArguments);
	}

}
