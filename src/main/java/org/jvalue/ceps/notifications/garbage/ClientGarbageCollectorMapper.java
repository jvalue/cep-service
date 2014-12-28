package org.jvalue.ceps.notifications.garbage;

import com.google.inject.Inject;

import org.jvalue.ceps.notifications.clients.ClientVisitor;
import org.jvalue.ceps.notifications.clients.GcmClient;
import org.jvalue.ceps.utils.Assert;


public final class ClientGarbageCollectorMapper implements ClientVisitor<Void, CollectionStatus> {

	private final ClientGarbageCollector gcmCollector;

	@Inject
	ClientGarbageCollectorMapper(
			ClientGarbageCollector gcmCollector) {

		Assert.assertNotNull(gcmCollector);
		this.gcmCollector = gcmCollector;
	}


	@Override
	public CollectionStatus visit(GcmClient client, Void param) {
		return gcmCollector.determineStatus(client.getDeviceId());
	}

}
