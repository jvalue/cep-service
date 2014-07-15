package org.jvalue.ceps.notifications.sender;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

import org.jvalue.ceps.utils.Log;


final class GcmApiKey {

	private final String key;

	public GcmApiKey(String resourceName) {
		URL resourceUrl = getClass().getResource(resourceName);

		String key = null;
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(new File(resourceUrl.toURI())));
			key = reader.readLine();
			if (reader.readLine() != null) Log.warn("ApiKey contains more than one line!");

		} catch(Exception e) {
			throw new IllegalArgumentException(e.getMessage());

		} finally {
			try {
				if (reader != null) reader.close();
			} catch(IOException ioc) {
				Log.error(ioc.getMessage());
			}
			this.key = key;
		}
	}


	@Override
	public String toString() {
		return key;
	}

}
