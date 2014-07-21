package org.jvalue.ceps.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.jvalue.ceps.utils.Log;
import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.MediaType;
import org.restlet.data.Protocol;
import org.restlet.routing.Router;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TextNode;


public final class DataManagerTest {

	private static final ObjectMapper mapper = new ObjectMapper();

	private static final String DATA_NAME = "dummy";
	private static final JsonNode DATA_TYPE = new TextNode("type");
	private static final JsonNode DATA_VALUE = new TextNode("value");

	private int onNewDataType = 0;
	private int onNewDataCount = 0;



	@Before
	public final void resetCounters() {
		onNewDataType = 0;
		onNewDataCount = 0;
	}


	@Test
	public final void testDataListener() throws Exception {
		DataManager manager = DummyDataManager.createInstance();
		DataChangeListener listener1 = new DummyDataChangeListener();
		DataChangeListener listener2 = new DummyDataChangeListener();
		manager.onSourceChanged(DATA_NAME, DATA_VALUE);

		assertEquals(0, onNewDataType);
		assertEquals(0, onNewDataCount);

		manager.registerDataListener(listener1);
		manager.registerDataListener(listener2);
		manager.onSourceChanged(DATA_NAME, DATA_VALUE);

		assertEquals(0, onNewDataType);
		assertEquals(2, onNewDataCount);

		manager.unregisterDataListener(listener1);
		manager.onSourceChanged(DATA_NAME, DATA_VALUE);
		
		assertEquals(0, onNewDataType);
		assertEquals(3, onNewDataCount);

		manager.unregisterDataListener(listener2);
		manager.onSourceChanged(DATA_NAME, DATA_VALUE);

		assertEquals(0, onNewDataType);
		assertEquals(3, onNewDataCount);
	}


	@Test
	public final void testMonitoring() throws Exception {

		final String CALLBACK_URL = "dummyUrl";
		final String CALLBACK_PARAM = "dummyParam";

		final String ODS_REGISTER = "notifications/rest/register";
		final String ODS_UNREGISTER = "notifications/unregister";
		final String ODS_SCHEMA = "schema";

		final String KEY_CLIENTID = "clientId";

		Application application = new Application() {

			@Override
			public Restlet createInboundRoot() {
				Router router =new Router(getContext());
				router.attachDefault(new Restlet() {

					private int callCount = 0;

					@Override
					public void handle(Request request, Response response) {

						switch(callCount) {
							case 0:
								assertTrue(request.getResourceRef().getPath()
									.contains(ODS_SCHEMA));

								response.setEntity(
									DATA_TYPE.toString(), 
									MediaType.APPLICATION_JSON);

								callCount++;
								break;

							case 1:
								try {
									assertTrue(request.getResourceRef().getPath()
										.contains(ODS_REGISTER));
									Map<String, Object> result = new HashMap<String, Object>();
									result.put(KEY_CLIENTID, "dummyClientId");
									response.setEntity(
											mapper.valueToTree(result).toString(), 
											MediaType.APPLICATION_JSON);
									callCount++;
								} catch (Exception e) {
									Log.info("fail", e);
								}

								break;

							case 2:
								assertTrue(request.getResourceRef().getPath()
									.contains(ODS_UNREGISTER));
								callCount++;
								assertEquals(
										"dummyClientId", 
										request.getResourceRef().getQueryAsForm()
											.getFirst(KEY_CLIENTID).getValue());
								break;

							default:
								fail();
						}
					}
				});

				return router;
			}
		};

		Component component = new Component();
		component.getServers().add(Protocol.HTTP, 8184);
		component.getDefaultHost().attach(application);
		component.start();

		DataManager manager = DummyDataManager.createInstance();
		DataSource source = new DataSource(DATA_NAME, "http://localhost:8184", ODS_SCHEMA);
		manager.registerDataListener(new DummyDataChangeListener());

		assertFalse(manager.isBeingMonitored(source));

		manager.startMonitoring(source, CALLBACK_URL, CALLBACK_PARAM);

		assertTrue(manager.isBeingMonitored(source));
		assertEquals(1, onNewDataType);
		assertEquals(0, onNewDataCount);

		manager.stopMonitoring(source);

		assertFalse(manager.isBeingMonitored(source));
		assertEquals(1, onNewDataType);
		assertEquals(0, onNewDataCount);

	}


	private class DummyDataChangeListener implements DataChangeListener {

		@Override
		public void onNewDataType(String name, JsonNode schema) {
			assertEquals(DATA_NAME, name);
			assertEquals(DATA_TYPE, schema);
			onNewDataType++;
		}

		@Override
		public void onNewData(String name, JsonNode data) {
			assertEquals(DATA_NAME, name);
			assertEquals(DATA_VALUE, data);
			onNewDataCount++;
		}

	}

}
