package org.jvalue.ceps.event;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.jvalue.ceps.utils.Assert;


public final class GarbageCollector {

	private final EventManager eventManager;
	private final long updateInterval, maxAge;
	private boolean isRunning = false;

	private ScheduledExecutorService scheduler;

	/**
	 * Times are measured in ms
	 */
	public GarbageCollector(EventManager eventManager, long updateInterval, long maxAge) {
		Assert.assertNotNull(eventManager);
		this.eventManager = eventManager;
		this.updateInterval = updateInterval;
		this.maxAge = maxAge;
	}


	public synchronized void start() {
		if (isRunning) throw new IllegalStateException("already running");
		isRunning = true;

		this.scheduler = Executors.newScheduledThreadPool(1);
		scheduler.scheduleAtFixedRate(
				new CleanTask(eventManager, maxAge),
				0,
				updateInterval,
				TimeUnit.MILLISECONDS);
	}


	public synchronized boolean isRunning() {
		return isRunning;
	}


	public synchronized void stop() {
		if (!isRunning) throw new IllegalStateException("not running");
		isRunning = false;

		scheduler.shutdownNow();
	}


	private static final class CleanTask implements Runnable {
		
		private final EventManager eventManager;
		private final long maxAge;

		public CleanTask(EventManager eventManager, long maxAge) {
			Assert.assertNotNull(eventManager);
			this.eventManager = eventManager;
			this.maxAge = maxAge;
		}

		@Override
		public void run() {
			long currentTimestamp = System.currentTimeMillis();
			for (Event event : eventManager.getAll()) {
				if (currentTimestamp - event.getTimestamp() >= maxAge)
					eventManager.removeEvent(event.getEventId());
			}
		}

	}

}
