package org.jvalue.ceps.notifications.garbage;


import org.jvalue.ceps.api.notifications.Client;

public interface ClientGarbageCollector<T extends Client> {

	public CollectionStatus determineStatus(T client);

}
