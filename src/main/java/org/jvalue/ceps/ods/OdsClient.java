package org.jvalue.ceps.ods;


public final class OdsClient extends OdsNotificationService.OdsClientDescription {

	private String id;

	public OdsClient() { }

	public OdsClient(String id, String callbackUrl, boolean sendData) {
		super(callbackUrl, sendData);
		this.id = id;
	}

	public String getId() {
		return id;
	}

}
