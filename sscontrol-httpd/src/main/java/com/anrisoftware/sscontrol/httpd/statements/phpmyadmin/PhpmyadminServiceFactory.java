package com.anrisoftware.sscontrol.httpd.statements.phpmyadmin;

import java.util.Map;

import com.anrisoftware.sscontrol.httpd.statements.webservice.WebServiceFactory;

/**
 * Factory to create the phpmyadmin service.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface PhpmyadminServiceFactory extends WebServiceFactory {

	@Override
	PhpmyadminService create(Map<String, Object> map);
}
