package org.jvalue.ceps.notifications.garbage;


final class GcmGarbageCollector implements GarbageCollector {

	@Override
	public CollectionStatus determineStatus(String deviceId) {
		return CollectionStatus.RETAIN;
	}

}
