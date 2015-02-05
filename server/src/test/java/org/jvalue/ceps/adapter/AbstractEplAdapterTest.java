package org.jvalue.ceps.adapter;


import org.junit.Assert;
import org.junit.Test;
import org.jvalue.ceps.api.adapter.EplAdapter;

import java.util.HashMap;
import java.util.Map;

public final class AbstractEplAdapterTest {

	private static final String adapterName = "dummyName";
	private static final Map<String, Class<?>> adapterArgs = new HashMap<>();

	private static final EplAdapter adapter = new DummyAdapter(adapterName, adapterArgs);

	static {
		adapterArgs.put("arg1", boolean.class);
		adapterArgs.put("arg2", String.class);
		adapterArgs.put("arg3", double.class);
		adapterArgs.put("arg4", int.class);
	}

	@Test
	public void testGetters() {
		Assert.assertEquals(adapterName, adapter.getId());
		Assert.assertEquals(adapterArgs, adapter.getRequiredParams());
	}


	@Test(expected = IllegalArgumentException.class)
	public void testMissingArg() {
		Map<String, Object> args = getValidArgs();
		args.remove("arg1");
		adapter.toEplStmt(args);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testAdditionalArg() {
		Map<String, Object> args = getValidArgs();
		args.put("arg5", "delete me");
		adapter.toEplStmt(args);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testInvalidArgType() {
		Map<String, Object> args = getValidArgs();
		args.put("arg1", "wrong type");
		adapter.toEplStmt(args);
	}


	private Map<String, Object> getValidArgs() {
		Map<String, Object> args = new HashMap<>();
		args.put("arg1", true);
		args.put("arg2", "hello world");
		args.put("arg3", 12.34);
		args.put("arg4", 1234);
		return args;
	}


	private static final class DummyAdapter extends AbstractEplAdapter {

		DummyAdapter(String adapterName, Map<String, Class<?>> adapterArgs) {
			super(adapterName, adapterArgs);
		}

		@Override
		protected String doToEplStmt(Map<String, Object> args) { return null; }
	}

}
