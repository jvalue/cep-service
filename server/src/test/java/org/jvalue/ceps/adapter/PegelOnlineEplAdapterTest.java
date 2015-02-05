package org.jvalue.ceps.adapter;


public final class PegelOnlineEplAdapterTest {

	/*
	@Test
	public void testEplStmtNotNull() {
		EplAdapter adapter = new PegelOnlineEplAdapter();
		Map<String, Class<?>> requiredArgs = adapter.getRequiredParams();

		// prepare dummy args
		Map<String, Object> args = new HashMap<>();
		for (String arg : requiredArgs.keySet()) {
			Class<?> argClass = requiredArgs.get(arg);
			args.put(arg, classToValue(argClass));
		}

		Assert.assertNotNull(adapter.toEplStmt(args));
	}


	private Object classToValue(Class<?> clas) {
		if (ClassUtils.isAssignable(clas, Number.class)) return 123;
		else if (ClassUtils.isAssignable(clas, Boolean.class)) return true;
		else if (ClassUtils.isAssignable(clas, String.class)) return "value";
		throw new IllegalArgumentException("unknown class " + clas.getSimpleName());
	}
	*/

}
