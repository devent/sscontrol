package com.anrisoftware.sscontrol.httpd.apache.linux;

import java.util.List;

import com.anrisoftware.sscontrol.httpd.statements.domain.Domain;
import com.anrisoftware.sscontrol.httpd.statements.webservice.WebService;

/**
 * Configures a web service.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface ServiceConfig {

	/**
	 * Returns the profile name of the service.
	 * 
	 * @return the profile {@link String} name.
	 */
	String getProfile();

	/**
	 * Returns the service name.
	 * 
	 * @return the service {@link String} name.
	 */
	String getServiceName();

	/**
	 * Sets the parent script with the properties.
	 * 
	 * @param script
	 *            the {@link ApacheScript}.
	 */
	void setScript(ApacheScript script);

	/**
	 * Returns the parent script with the properties.
	 * 
	 * @return the {@link ApacheScript}.
	 */
	ApacheScript getScript();

	/**
	 * Creates the domain configuration and configures the service.
	 * 
	 * @param domain
	 *            the {@link Domain}.
	 * 
	 * @param service
	 *            the {@link WebService}.
	 * 
	 * @param config
	 *            the {@link List} of the domain configuration.
	 */
	void deployService(Domain domain, WebService service, List<String> config);
}
