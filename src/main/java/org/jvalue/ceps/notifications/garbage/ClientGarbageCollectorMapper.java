package org.jvalue.ceps.notifications.garbage;

import com.google.inject.Inject;

import org.jvalue.ceps.notifications.clients.ClientVisitor;
import org.jvalue.ceps.notifications.clients.GcmClient;


/**
 * Maps instances of {@link org.jvalue.ceps.notifications.garbage.ClientGarbageCollector} to
 * instances of {@link org.jvalue.ceps.notifications.clients.Client}.
 */
public final class ClientGarbageCollectorMapper implements ClientVisitor<Void, CollectionStatus> {

	private final ClientGarbageCollector<GcmClient> gcmCollector;

	@Inject
	ClientGarbageCollectorMapper(ClientGarbageCollector<GcmClient> gcmCollector) {
		this.gcmCollector = gcmCollector;
	}


	@Override
	public CollectionStatus visit(GcmClient client, Void param) {
		return gcmCollector.determineStatus(client);
	}

}
