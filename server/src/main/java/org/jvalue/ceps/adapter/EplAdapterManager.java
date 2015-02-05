package org.jvalue.ceps.adapter;


import com.google.inject.Inject;

import org.apache.commons.lang3.ClassUtils;
import org.jvalue.ceps.api.adapter.ArgumentType;
import org.jvalue.ceps.api.adapter.EplAdapter;
import org.jvalue.ceps.db.EplAdapterRepository;
import org.jvalue.ceps.utils.Assert;

import java.util.List;
import java.util.Map;

public final class EplAdapterManager {

	private final EplAdapterRepository adapterRepository;

	@Inject
	EplAdapterManager(EplAdapterRepository adapterRepository) {
		this.adapterRepository = adapterRepository;
	}


	public EplAdapter get(String adapterId) {
		return adapterRepository.findById(adapterId);
	}


	public List<EplAdapter> getAll() {
		return adapterRepository.getAll();
	}


	public void remove(EplAdapter adapter) {
		adapterRepository.remove(adapter);
	}


	public void add(EplAdapter adapter) {
		adapterRepository.add(adapter);
	}


	/**
	 * Creates an EPL statement by substituting keys in the adapter EPL blueprint with values
	 * provided by clients.
	 */
	public final String createEplStatement(EplAdapter adapter, Map<String, Object> clientArgs) {
		assertAreValidClientArgs(adapter, clientArgs);

		String eplStmt = adapter.getEplBlueprint();
		for (Map.Entry<String, Object> arg : clientArgs.entrySet()) {
			eplStmt = eplStmt.replaceAll(arg.getKey(), arg.getValue().toString());
		}
		return eplStmt;
	}


	/**
	 * Checks the client args for their type and throws an {@link java.lang.IllegalAccessException}
	 * with a human readable error message in case or errors.
	 */
	public void assertAreValidClientArgs(EplAdapter adapter, Map<String, Object> clientArgs) {
		Map<String, ArgumentType> requiredArgs = adapter.getRequiredArguments();
		Assert.assertEquals(requiredArgs.size(), clientArgs.size(), "found " + clientArgs.size() + " args but required " + requiredArgs.size());
		for (String key : requiredArgs.keySet()) {
			Assert.assertNotNull(clientArgs.get(key), "missing required param " + key);
			Assert.assertTrue(ClassUtils.isAssignable(clientArgs.get(key).getClass(), requiredArgs.get(key).getJavaType(), true), "invalid arg type for " + key);
		}
	}

}
