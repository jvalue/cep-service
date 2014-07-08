package org.jvalue.ceps.rest;

import java.util.Map;

import org.restlet.Restlet;


public interface RestApi {

	public Map<String, Restlet> getRoutes();

}
