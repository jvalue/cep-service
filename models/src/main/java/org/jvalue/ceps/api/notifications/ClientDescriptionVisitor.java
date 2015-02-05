package org.jvalue.ceps.api.notifications;

public interface ClientDescriptionVisitor<P,R> {

	public R visit(GcmClientDescription clientDescription, P param);
	public R visit(HttpClientDescription clientDescription, P param);

}
