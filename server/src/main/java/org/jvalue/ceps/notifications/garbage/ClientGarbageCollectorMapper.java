package org.jvalue.ceps.notifications.garbage;

import com.google.inject.Inject;

import org.jvalue.ceps.notifications.clients.ClientVisitor;
import org.jvalue.ceps.notifications.clients.GcmClient;
import org.jvalue.ceps.notifications.clients.HttpClient;


/**
 * Maps instances of {@link org.jvalue.ceps.notifications.garbage.ClientGarbageCollector} to
 * instances of {@link org.jvalue.ceps.notifications.clients.Client}.
 */
public final class ClientGarbageCollectorMapper implements ClientVisitor<Void, CollectionStatus> {

	private final ClientGarbageCollector<GcmClient> gcmCollector;
	private final ClientGarbageCollector<HttpClient> httpCollector;

	@Inject
	ClientGarbageCollectorMapper(
			ClientGarbageCollector<GcmClient> gcmCollector,
			ClientGarbageCollector<HttpClient>  httpCollector) {

		this.gcmCollector = gcmCollector;
		this.httpCollector = httpCollector;
	}


	@Override
	public CollectionStatus visit(GcmClient client, Void param) {
		return gcmCollector.determineStatus(client);
	}


	@Override
	public CollectionStatus visit(HttpClient client, Void param) {
		return httpCollector.determineStatus(client);
	}

}
