package org.jvalue.ceps.notifications.garbage;



public interface GarbageCollector {

	public CollectionStatus determineStatus(String deviceId);

}
