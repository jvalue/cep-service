package org.jvalue.ceps.esper;

import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPStatement;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.common.base.Optional;
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

	private final Map<String, EPStatement> startedStatements = new HashMap<>();

	private Optional<SourceData> lastData = Optional.absent();

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
		List<EventDefinition> definitions = schemaTranslator.toEventDefinition(sourceId, schema);
		for (EventDefinition definition : definitions) {
			int stmtCount = admin.getConfiguration().getEventTypeNameUsedBy(definition.getName()).size();
			if (stmtCount > 0) Log.warn("Removing event definition " + definition.getName() + " while " + stmtCount + " statement were still using it");
			admin.getConfiguration().removeEventType(definition.getName(), true);
		}
	}


	@Override
	public void onRestoreSources(Map<String, JsonNode> sources) {
		for (Map.Entry<String, JsonNode> entry : sources.entrySet()) {
			onSourceAdded(entry.getKey(), entry.getValue());
		}
	}


	@Override
	@SuppressWarnings("rawTypes")
	public void onNewSourceData(String sourceId, ArrayNode jsonData) {
		Assert.assertNotNull(sourceId, jsonData);

		// ensures that EPL statements which have been added AFTER this data can also see those events (required by PegelAlarm)
		if (lastData.isPresent()) onNewSourceData(lastData.get());

		Map[] data = new Map[jsonData.size()];
		for (int i = 0; i < data.length; i++) {
			try {
				data[i] = dataTranslator.toMap(jsonData.get(i));
			} catch (IOException ioe) {
				Log.error("failed to translate data " + sourceId, ioe);
			}
		}
		lastData = Optional.of(new SourceData(sourceId, data));
		onNewSourceData(lastData.get());
	}


	@SuppressWarnings("rawTypes")
	private void onNewSourceData(SourceData sourceData) {
		for (Map data : sourceData.data) {
			runtime.sendEvent(data, sourceData.sourceId);
		}
	}


	private static class SourceData {

		private final String sourceId;
		@SuppressWarnings("rawTypes") private final Map[] data;

		@SuppressWarnings("rawTypes")
		public SourceData(String sourceId, Map[] data) {
			this.sourceId = sourceId;
			this.data = data;
		}

	}

}
