package org.jvalue.ceps.notifications.sender;

import org.jvalue.ceps.notifications.clients.Client;
import org.jvalue.ceps.utils.Assert;



public final class SenderResult {

	public enum Status {
		SUCCESS,
		UPDATE_CLIENT,
		REMOVE_CLIENT,
		ERROR;
	}


	private final Status status;
	private final Client oldClient;
	private final Client newClient;
	private final Throwable errorCause;
	private final String errorMsg;
	

	private SenderResult(
			Status status,
			Client oldClient,
			Client newClient,
			Throwable errorCause,
			String errorMsg) {

		this.status = status;
		this.oldClient = oldClient;
		this.newClient = newClient;
		this.errorCause = errorCause;
		this.errorMsg = errorMsg;
	}


	public Status getStatus() {
		return status;
	}


	public Client getOldClient() {
		return oldClient;
	}


	public Client getNewClient() {
		return newClient;
	}
	

	public Throwable getErrorCause() {
		return errorCause;
	}


	public String getErrorMsg() {
		return errorMsg;
	}


	static class Builder {
		private final Status status;
		private Client oldClient, newClient;
		private Throwable errorCause;
		private String errorMsg;


		public Builder(Status status) {
			Assert.assertNotNull(status);
			this.status = status;
		}


		public Builder oldClient(Client oldClient) {
			this.oldClient = oldClient;
			return this;
		}


		public Builder newClient(Client newClient) {
			this.newClient = newClient;
			return this;
		}


		public Builder errorCause(Throwable errorCause) {
			this.errorCause = errorCause;
			return this;
		}


		public Builder errorMsg(String errorMsg) {
			this.errorMsg = errorMsg;
			return this;
		}


		public SenderResult build() {
			return new SenderResult(status, oldClient, newClient, errorCause, errorMsg);
		}
	}

}
