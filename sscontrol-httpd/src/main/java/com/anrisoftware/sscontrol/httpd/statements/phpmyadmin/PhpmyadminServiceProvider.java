package com.anrisoftware.sscontrol.httpd.statements.phpmyadmin;

import javax.inject.Inject;
import javax.inject.Provider;

import com.anrisoftware.sscontrol.httpd.statements.webservice.WebServiceFactory;

public class PhpmyadminServiceProvider implements Provider<WebServiceFactory> {

	@Inject
	private PhpmyadminServiceFactory factory;

	@Override
	public WebServiceFactory get() {
		return factory;
	}

}
