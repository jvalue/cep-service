package org.jvalue.ceps.esper;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.jvalue.ceps.data.DataChangeListener;
import org.jvalue.ceps.utils.Assert;
import org.jvalue.ceps.utils.Log;

import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.fasterxml.jackson.databind.JsonNode;


public final class EsperManager implements DataChangeListener {

	private static EsperManager instance;

	public static EsperManager getInstance() {
		if (instance == null) instance = new EsperManager();
		return instance;
	}


	private final EPRuntime runtime;
	private final EPAdministrator admin;
	private final Map<String, EPStatement> startedStatements = new HashMap<String, EPStatement>();

	private EsperManager() {
		EPServiceProvider provider = EPServiceProviderManager.getDefaultProvider();
		runtime = provider.getEPRuntime();
		admin = provider.getEPAdministrator();
	}


	public String register(String eplStatement, JsonUpdateListener listener) {
		Assert.assertNotNull(eplStatement, listener);

		String stmtId = UUID.randomUUID().toString();
		EPStatement stmt = admin.createEPL(eplStatement);
		stmt.addListener(new EsperUpdateListener(listener, stmtId));
		startedStatements.put(stmtId, stmt);

		return stmtId;
	}


	public void unregister(String registerId) {
		Assert.assertNotNull(registerId);

		EPStatement stmt = startedStatements.get(registerId);
		if (stmt == null) throw new IllegalArgumentException("not registered");

		stmt.destroy();
		startedStatements.remove(registerId);
	}


	@Override
	public void onNewDataType(String dataName, JsonNode dataSchema) {
		List<EventDefinition> definitions = SchemaTranslator.toEventDefinition(dataName, dataSchema);
		for (EventDefinition definition : definitions) {
			admin.getConfiguration().addEventType(definition.getName(), definition.getSchema());
		}
	}


	@Override
	public void onNewData(String dataName, JsonNode data) {
		Assert.assertNotNull(dataName, data);
		Assert.assertTrue(data.isArray());

		for (int i = 0; i < data.size(); i++) {
			try {
				runtime.sendEvent(DataTranslator.toMap(data.get(i)), dataName);
			} catch (IOException ioe) {
				Log.error("failed to translate data " + dataName, ioe);
			}
		}
	}

}
