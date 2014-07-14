package org.jvalue.ceps.client;

import org.jvalue.ceps.utils.Assert;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;


final class ClientUpdateListener implements UpdateListener {

	private final String clientId;


	public ClientUpdateListener(String clientId) {
		Assert.assertNotNull(clientId);
		this.clientId = clientId;
	}


	public String getClientId() {
		return clientId;
	}


	@Override
	public void update(EventBean[] newEvents, EventBean[] oldEvents) {
	}

}
