package com.anrisoftware.sscontrol.httpd.statements.webservice;

import java.util.Map;

public interface WebServiceFactory {

	WebService create(Map<String, Object> map);
}
