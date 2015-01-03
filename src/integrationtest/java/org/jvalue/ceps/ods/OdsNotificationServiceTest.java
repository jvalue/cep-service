package org.jvalue.ceps.ods;


import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import retrofit.RetrofitError;

public final class OdsNotificationServiceTest extends AbstractOdsServiceTest {

	private static final String CLIENT_ID = "someClientId";
	private static final String CALLBACK_URL = "http://localhost:12345";
	private static final boolean SEND_DATA = true;

	private OdsNotificationService notificationService;


	@Before
	public void setupSource() {
		addSource();
		this.notificationService = createService(OdsNotificationService.class);
	}


	@After
	public void tearDownSource() {
		removeSource();
	}


	@Test
	public void testNotificationsSource() {
		// create
		OdsClientDescription clientDescription = new OdsClientDescription(CALLBACK_URL, SEND_DATA);
		OdsClient client = notificationService.register(SOURCE_ID, CLIENT_ID, clientDescription);
		assertEquals(client);

		// get
		client = notificationService.get(SOURCE_ID, CLIENT_ID);
		assertEquals(client);

		// remove
		notificationService.unregister(SOURCE_ID, CLIENT_ID);
		try {
			notificationService.get(SOURCE_ID, CLIENT_ID);
		} catch (RetrofitError re) {
			Assert.assertEquals(404, re.getResponse().getStatus());
			return;
		}
		Assert.fail("source was not removed");
	}


	private void assertEquals(OdsClient client) {
		Assert.assertEquals(CLIENT_ID, client.getId());
		Assert.assertEquals(CALLBACK_URL, client.getCallbackUrl());
		Assert.assertEquals(SEND_DATA, client.getSendData());
	}

}
