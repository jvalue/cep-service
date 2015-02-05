package org.jvalue.ceps.notifications.clients;


public interface ClientVisitor<P,R> {

	public R visit(GcmClient client, P param);
	public R visit(HttpClient client, P param);

}
