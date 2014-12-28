package org.jvalue.ceps.notifications.garbage;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import org.jvalue.ceps.notifications.NotificationManager;
import org.jvalue.ceps.notifications.NotificationsModule;
import org.jvalue.ceps.notifications.clients.Client;
import org.jvalue.ceps.notifications.clients.ClientVisitor;
import org.jvalue.ceps.utils.Log;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import io.dropwizard.lifecycle.Managed;


public final class ClientGarbageCollectorManager implements Managed {

	private final NotificationManager notificationManager;
	private final ClientVisitor<Void, CollectionStatus> mapper;
	private final long interval;

	private ScheduledExecutorService scheduler;

	/**
	 * @param interval initial delay and run interval in ms
	 */
	@Inject
	ClientGarbageCollectorManager(
			NotificationManager notificationManager,
			ClientVisitor<Void, CollectionStatus> mapper,
			@Named(NotificationsModule.GCM_GARBAGE_COLLECTOR_PERIOD) long interval) {

		this.notificationManager = notificationManager;
		this.mapper = mapper;
		this.interval = interval;
	}


	@Override
	public void start() {
		scheduler = Executors.newScheduledThreadPool(1);
		scheduler.scheduleAtFixedRate(
				new CollectionRunnable(notificationManager, mapper),
				interval,
				interval,
				TimeUnit.MILLISECONDS);
	}


	@Override
	public void stop() {
		scheduler.shutdownNow();
		scheduler = null;
	}


	private static final class CollectionRunnable implements Runnable {

		private final NotificationManager notificationManager;
		private final ClientVisitor<Void, CollectionStatus> mapper;

		public CollectionRunnable(NotificationManager notificationManager, ClientVisitor<Void, CollectionStatus> mapper) {
			this.notificationManager = notificationManager;
			this.mapper = mapper;
		}

		
		@Override
		public void run() {
			Log.info("Running clients garbage collection");
			Set<String> visitedDevices = new HashSet<String>();

			for (Client client : notificationManager.getAll()) {
				String deviceId = client.getDeviceId();
				if (visitedDevices.contains(deviceId)) continue;

				CollectionStatus status = client.accept(mapper, null);
				if (status.equals(CollectionStatus.COLLECT)) {
					Log.info("Removing device " + deviceId);
					notificationManager.unregisterDevice(deviceId);
				}

				visitedDevices.add(deviceId);
			}
			Log.info("Finished client garbage collection");
		}
	}

}
