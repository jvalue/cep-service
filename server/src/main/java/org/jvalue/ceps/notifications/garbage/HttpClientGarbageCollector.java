package org.jvalue.ceps.notifications.garbage;

import com.google.inject.Inject;

import org.jvalue.ceps.notifications.clients.HttpClient;


final class HttpClientGarbageCollector implements ClientGarbageCollector<HttpClient> {

	@Inject
	HttpClientGarbageCollector() { }


	@Override
	public CollectionStatus determineStatus(HttpClient client) {
		// TODO currently not supported
		return CollectionStatus.RETAIN;
	}

}
