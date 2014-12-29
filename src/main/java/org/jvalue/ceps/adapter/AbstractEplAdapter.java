package org.jvalue.ceps.adapter;

import org.apache.commons.lang3.ClassUtils;
import org.jvalue.ceps.utils.Assert;

import java.util.Map;


abstract class AbstractEplAdapter implements EplAdapter {

	private final String name;
	private final Map<String, Class<?>> requiredParams;

	protected AbstractEplAdapter(String name, Map<String, Class<?>> requiredParams) {
		this.name = name;
		this.requiredParams = requiredParams;
	}


	@Override
	public final String getName() {
		return name;
	}


	@Override
	public final Map<String, Class<?>> getRequiredParams() {
		return requiredParams;
	}


	@Override
	public final String toEplStmt(Map<String, Object> params) {
		Assert.assertEquals(requiredParams.size(), params.size(), "found " + params.size() + " args but required " + requiredParams.size());
		for (String key : requiredParams.keySet()) {
			Assert.assertNotNull(params.get(key), "missing required param " + key);
			Assert.assertTrue(ClassUtils.isAssignable(params.get(key).getClass(),requiredParams.get(key), true), "invalid arg type for " + key);
		}
		return doToEplStmt(params);
	}


	/**
	 * @param params valid arguments according to specification of {@link AbstractEplAdapter#getRequiredParams()}
	 */
	protected abstract String doToEplStmt(Map<String, Object> params);

}
