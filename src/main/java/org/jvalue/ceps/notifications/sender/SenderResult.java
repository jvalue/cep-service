package org.jvalue.ceps.notifications.sender;

import org.jvalue.ceps.utils.Assert;
import org.jvalue.ceps.utils.Pair;



public final class SenderResult {

	public enum Status {
		SUCCESS,
		UPDATE_CLIENT,
		REMOVE_CLIENT,
		ERROR;
	}


	private final Status status;
	private final String removeDeviceId;
	private final Pair<String, String> updateDeviceId;
	private final Throwable errorCause;
	private final String errorMsg;
	

	private SenderResult(
			Status status,
			String removeDeviceId,
			Pair<String, String> updateDeviceId,
			Throwable errorCause,
			String errorMsg) {

		this.status = status;
		this.removeDeviceId = removeDeviceId;
		this.updateDeviceId = updateDeviceId;
		this.errorCause = errorCause;
		this.errorMsg = errorMsg;
	}


	public Status getStatus() {
		return status;
	}

	
	public String getRemoveDeviceId() {
		return removeDeviceId;
	}


	public Pair<String, String> getUpdateDeviceId() {
		return updateDeviceId;
	}


	public Throwable getErrorCause() {
		return errorCause;
	}


	public String getErrorMsg() {
		return errorMsg;
	}


	static class Builder {
		private final Status status;
		private String removeDeviceId;
		private Pair<String, String> updateDeviceId;
		private Throwable errorCause;
		private String errorMsg;


		public Builder(Status status) {
			Assert.assertNotNull(status);
			this.status = status;
		}


		public Builder removeDeviceId(String removeDeviceId) {
			this.removeDeviceId = removeDeviceId;
			return this;
		}


		public Builder updateDeviceId(String oldDeviceId, String newDeviceId) {
			this.updateDeviceId = new Pair<String, String>(oldDeviceId, newDeviceId);
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
			return new SenderResult(status, removeDeviceId, updateDeviceId, errorCause, errorMsg);
		}
	}

}
