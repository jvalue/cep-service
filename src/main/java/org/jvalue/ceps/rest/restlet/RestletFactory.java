package org.jvalue.ceps.rest.restlet;

import org.jvalue.ceps.event.EventManager;
import org.restlet.Restlet;


public final class RestletFactory {

	private RestletFactory() { }


	public static Restlet createDefaultRestlet() {
		return new DefaultRestlet();
	}


	public static Restlet createGcmRegisterRestlet() {
		return new GcmRegisterRestlet();
	}


	public static Restlet createUnregisterRestlet() {
		return new NotificationUnregisterRestlet();
	}


	public static Restlet createFetchEventRestlet() {
		return new FetchEventRestlet(EventManager.getInstance());
	}


	public static Restlet createRemoveEventRestlet() {
		return new RemoveEventRestlet(EventManager.getInstance());
	}

}
