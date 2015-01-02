package org.jvalue.ceps.ods;

import javax.validation.constraints.NotNull;


public class OdsClientDescription {

	private final String type = "HTTP";
	@NotNull private final String callbackUrl;
	@NotNull private final boolean sendData;

	public OdsClientDescription(String callbackUrl, boolean sendData) {
		this.callbackUrl = callbackUrl;
		this.sendData = sendData;
	}

	public String getCallbackUrl() {
		return callbackUrl;
	}

	public boolean getSendData() {
		return sendData;
	}

	public String getType() {
		return type;
	}

}
