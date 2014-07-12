package org.jvalue.ceps.esper;

import java.util.HashMap;
import java.util.Map;

import org.jvalue.ceps.client.Client;
import org.jvalue.ceps.utils.Assert;

import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.fasterxml.jackson.databind.JsonNode;


public final class EsperManager {

	private static EsperManager instance;

	public static EsperManager getInstance() {
		if (instance == null) instance = new EsperManager();
		return instance;
	}


	private final EPRuntime runtime;
	private final EPAdministrator administrator;
	private final Map<Client, String> startedStatements = new HashMap<Client, String>();

	private EsperManager() {
		EPServiceProvider provider = EPServiceProviderManager.getDefaultProvider();
		runtime = provider.getEPRuntime();
		administrator = provider.getEPAdministrator();
	}


	public void register(Client client, EsperEventListener listener) {
		Assert.assertNotNull(client, listener);

		EPStatement stmt = administrator.createEPL(client.getEplStatement());
		stmt.addListener(new EsperUpdateListener(client, listener));

		startedStatements.put(client, stmt.getName());
	}


	public void unregister(Client client) {
		Assert.assertNotNull(client);

		String stmtName = startedStatements.get(client);
		if (stmtName == null) throw new IllegalArgumentException("client not registered");
		EPStatement stmt = administrator.getStatement(stmtName);
		if (stmt == null) throw new IllegalArgumentException("client not registered");

		stmt.destroy();
		startedStatements.remove(client);
	}


	public void addData(JsonNode data) { }

}
