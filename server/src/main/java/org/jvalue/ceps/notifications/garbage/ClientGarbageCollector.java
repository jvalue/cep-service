package org.jvalue.ceps.notifications.garbage;


import org.jvalue.ceps.notifications.clients.Client;

public interface ClientGarbageCollector<T extends Client> {

	public CollectionStatus determineStatus(T client);

}
