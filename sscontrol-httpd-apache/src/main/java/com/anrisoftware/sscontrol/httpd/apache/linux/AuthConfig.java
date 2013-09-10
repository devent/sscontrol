package com.anrisoftware.sscontrol.httpd.apache.linux;

import java.util.List;

import com.anrisoftware.sscontrol.httpd.statements.auth.AbstractAuth;
import com.anrisoftware.sscontrol.httpd.statements.domain.Domain;

/**
 * HTTP/authentication.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface AuthConfig {

	/**
	 * Returns the profile name of the authentication.
	 * 
	 * @return the profile {@link String} name.
	 */
	String getProfile();

	/**
	 * Returns the authentication name.
	 * 
	 * @return the authentication {@link String} name.
	 */
	String getAuthName();

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
	 * Creates the domain configuration and configures the authentication.
	 * 
	 * @param domain
	 *            the {@link Domain}.
	 * 
	 * @param auth
	 *            the {@link AbstractAuth}.
	 * 
	 * @param config
	 *            the {@link List} of the domain configuration.
	 */
	void deployAuth(Domain domain, AbstractAuth auth, List<String> config);
}
