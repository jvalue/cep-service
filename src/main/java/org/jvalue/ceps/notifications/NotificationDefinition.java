package org.jvalue.ceps.notifications;


interface NotificationDefinition<C extends Client> {

	public NotificationSender<C> getSender();

}
