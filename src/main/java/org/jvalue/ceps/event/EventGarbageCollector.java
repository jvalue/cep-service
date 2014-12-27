package org.jvalue.ceps.event;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import org.jvalue.ceps.utils.Assert;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import io.dropwizard.lifecycle.Managed;


public final class EventGarbageCollector implements Managed {

	private final EventManager eventManager;
	private final long updateInterval, maxAge;
	private final ScheduledExecutorService scheduler;

	/**
	 * Times are measured in ms
	 */
	@Inject
	public EventGarbageCollector(
			EventManager eventManager,
			@Named(EventModule.EVENT_GARBAGE_COLLECTOR_PERIOD) long updateInterval,
			@Named(EventModule.EVENT_GARBAGE_COLLECTOR_MAX_AGE) long maxAge) {

		Assert.assertNotNull(eventManager);
		this.eventManager = eventManager;
		this.updateInterval = updateInterval;
		this.maxAge = maxAge;
		this.scheduler = Executors.newScheduledThreadPool(1);
	}


	@Override
	public void start() {
		scheduler.scheduleAtFixedRate(
				new CleanTask(eventManager, maxAge),
				0,
				updateInterval,
				TimeUnit.MILLISECONDS);
	}


	@Override
	public void stop() {
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
