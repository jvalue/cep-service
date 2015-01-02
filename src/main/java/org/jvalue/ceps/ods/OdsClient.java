package org.jvalue.ceps.ods;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class OdsClient extends OdsClientDescription {

	private final String id;

	@JsonCreator
	public OdsClient(
			@JsonProperty("id") String id,
			@JsonProperty("callbackUrl") String callbackUrl,
			@JsonProperty("sendData") boolean sendData) {

		super(callbackUrl, sendData);
		this.id = id;
	}

	public String getId() {
		return id;
	}

}
