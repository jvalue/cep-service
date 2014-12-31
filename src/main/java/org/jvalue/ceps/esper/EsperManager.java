package org.jvalue.ceps.esper;

import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPStatement;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.inject.Inject;

import org.jvalue.ceps.data.DataUpdateListener;
import org.jvalue.ceps.utils.Assert;
import org.jvalue.ceps.utils.Log;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public final class EsperManager implements DataUpdateListener {

	private final EPRuntime runtime;
	private final EPAdministrator admin;

	private final SchemaTranslator schemaTranslator;
	private final DataTranslator dataTranslator;

	private final Map<String, EPStatement> startedStatements = new HashMap<String, EPStatement>();

	@Inject
	EsperManager(
			EPServiceProvider provider,
			SchemaTranslator schemaTranslator,
			DataTranslator dataTranslator) {

		this.runtime = provider.getEPRuntime();
		this.admin = provider.getEPAdministrator();
		this.schemaTranslator = schemaTranslator;
		this.dataTranslator = dataTranslator;
	}


	public String register(String eplStatement, EventUpdateListener listener) {
		Assert.assertNotNull(eplStatement, listener);

		String registrationId = UUID.randomUUID().toString();
		EPStatement stmt = admin.createEPL(eplStatement);
		stmt.addListener(new EventUpdateListenerAdapter(listener, registrationId));
		startedStatements.put(registrationId, stmt);

		return registrationId;
	}


	public void unregister(String registrationId) {
		Assert.assertNotNull(registrationId);

		EPStatement stmt = startedStatements.get(registrationId);
		if (stmt == null) throw new IllegalArgumentException("not registered");

		stmt.destroy();
		startedStatements.remove(registrationId);
	}


	@Override
	public void onSourceAdded(String sourceId, JsonNode schema) {
		Log.info("Adding datatype \"" + sourceId + "\"");
		List<EventDefinition> definitions = schemaTranslator.toEventDefinition(sourceId, schema);
		for (EventDefinition definition : definitions) {
			admin.getConfiguration().addEventType(definition.getName(), definition.getSchema());
		}
	}


	@Override
	public void onSourceRemoved(String sourceId, JsonNode schema) {
		// TODO remove from Esper engine?
		throw new UnsupportedOperationException("not implemented");
	}


	@Override
	public void onRestoreSources(Map<String, JsonNode> sources) {
		for (Map.Entry<String, JsonNode> entry : sources.entrySet()) {
			onSourceAdded(entry.getKey(), entry.getValue());
		}
	}


	@Override
	public void onNewSourceData(String sourceId, ArrayNode data) {
		Assert.assertNotNull(sourceId, data);
		for (int i = 0; i < data.size(); i++) {
			try {
				runtime.sendEvent(dataTranslator.toMap(data.get(i)), sourceId);
			} catch (IOException ioe) {
				Log.error("failed to translate data " + sourceId, ioe);
			}
		}
	}

}
