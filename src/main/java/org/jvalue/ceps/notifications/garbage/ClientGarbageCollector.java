package org.jvalue.ceps.notifications.garbage;



public interface ClientGarbageCollector {

	public CollectionStatus determineStatus(String deviceId);

}
