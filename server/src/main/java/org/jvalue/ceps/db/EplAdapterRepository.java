package org.jvalue.ceps.db;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import org.ektorp.CouchDbConnector;
import org.ektorp.DocumentNotFoundException;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.View;
import org.jvalue.ceps.api.adapter.EplAdapter;
import org.jvalue.common.db.DbDocument;
import org.jvalue.common.db.DbDocumentAdaptable;
import org.jvalue.common.db.RepositoryAdapter;

import java.util.List;

public final class EplAdapterRepository extends RepositoryAdapter<
		EplAdapterRepository.EplAdapterCouchDbRepository,
		EplAdapterRepository.EplAdapterDocument,
		EplAdapter> {

	static final String DATABASE_NAME = "eplAdapter";
	private static final String DOCUMENT_ID = "doc.value.id != null && doc.value.eplBlueprint != null";

	@Inject
	EplAdapterRepository(@Named(DATABASE_NAME) CouchDbConnector connector) {
		super(new EplAdapterCouchDbRepository(connector));
	}


	@View( name = "all", map = "function(doc) { if (" + DOCUMENT_ID + ") emit(null, doc) }")
	static final class EplAdapterCouchDbRepository
			extends CouchDbRepositorySupport<EplAdapterDocument>
			implements DbDocumentAdaptable<EplAdapterDocument, EplAdapter> {


		EplAdapterCouchDbRepository(CouchDbConnector connector) {
			super(EplAdapterDocument.class, connector);
			initStandardDesignDocument();
		}


		@Override
		@View(name = "by_id", map = "function(doc) { if (" + DOCUMENT_ID + ") emit(doc.value.id, doc._id) }")
		public EplAdapterDocument findById(String adapterId) {
			List<EplAdapterDocument> adapters = queryView("by_id", adapterId);
			if (adapters.isEmpty()) throw new DocumentNotFoundException("no adapter with id " + adapterId);
			if (adapters.size() == 1) return adapters.get(0);
			throw new IllegalStateException("found more than one adapter for id " + adapterId);
		}


		@Override
		public EplAdapterDocument createDbDocument(EplAdapter adapter) {
			return new EplAdapterDocument(adapter);
		}


		@Override
		public String getIdForValue(EplAdapter adapter) {
			return adapter.getId();
		}

	}

	static final class EplAdapterDocument extends DbDocument<EplAdapter> {

		@JsonCreator
		public EplAdapterDocument(
				@JsonProperty("value") EplAdapter eplAdapter) {
			super(eplAdapter);
		}

	}
}
