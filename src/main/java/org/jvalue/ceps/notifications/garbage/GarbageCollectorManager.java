package org.jvalue.ceps.notifications.garbage;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.jvalue.ceps.notifications.NotificationManager;
import org.jvalue.ceps.notifications.clients.Client;
import org.jvalue.ceps.notifications.clients.ClientVisitor;
import org.jvalue.ceps.notifications.clients.GcmClient;
import org.jvalue.ceps.utils.Assert;


public final class GarbageCollectorManager {

	private final NotificationManager notificationManager;
	private final long interval;

	private boolean running = false;
	private ScheduledExecutorService scheduler;

	/**
	 * @param interval initial delay and run interval in ms
	 */
	public GarbageCollectorManager(NotificationManager notificationManager, long interval) {
		Assert.assertNotNull(notificationManager);
		this.notificationManager = notificationManager;
		this.interval = interval;
	}


	public void startCollection() {
		if (running) throw new IllegalStateException("already running");
		running = true;

		scheduler = Executors.newScheduledThreadPool(1);
		scheduler.scheduleAtFixedRate(
				new Runnable() {
					@Override
					public void run() {
						// collect unused device ids
						GarbageCollectorVisitor garbageVisitor = new GarbageCollectorVisitor();
						for (Client client : notificationManager.getAll()) {
							client.accept(garbageVisitor, null);
						}

						// remove ids
						for (String deviceId : garbageVisitor.unusedDeviceIds) {
							notificationManager.unregisterDevice(deviceId);
						}
					}
				},
				interval,
				interval,
				TimeUnit.MILLISECONDS);
	}


	public void stopCollection() {
		if (!running) throw new IllegalStateException("already running");
		running = false;

		scheduler.shutdownNow();
		scheduler = null;
	}


	private static final class GarbageCollectorVisitor implements ClientVisitor<Void, Void> {

		private final Set<String> unusedDeviceIds = new HashSet<String>();
		
		private final GcmGarbageCollector gcmCollector = new GcmGarbageCollector();
		private final Set<String> gcmClients = new HashSet<String>();

		@Override
		public Void visit(GcmClient client, Void param) {
			String deviceId = client.getDeviceId();
			
			if (!gcmClients.contains(deviceId)) {
				if (gcmCollector.determineStatus(deviceId) == CollectionStatus.COLLECT) {
					unusedDeviceIds.add(deviceId);
				}
			}

			gcmClients.add(client.getDeviceId());
			return null;
		}

	}

}
