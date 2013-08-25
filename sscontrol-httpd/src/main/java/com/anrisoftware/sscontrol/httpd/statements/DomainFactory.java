package com.anrisoftware.sscontrol.httpd.statements;

import java.util.Map;

/**
 * Factory to create a new domain.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface DomainFactory {

	/**
	 * Creates the domain with the specified name.
	 * 
	 * @param args
	 *            the {@link Map} arguments of the domain.
	 * 
	 * @param name
	 *            the {@link String} name of the domain.
	 * 
	 * @return the {@link Domain}.
	 */
	Domain create(Map<String, Object> args, String name);

	/**
	 * Creates the SSL/TLS domain with the specified name.
	 * 
	 * @param args
	 *            the {@link Map} arguments of the domain.
	 * 
	 * @param name
	 *            the {@link String} name of the domain.
	 * 
	 * @return the {@link Domain}.
	 */
	SslDomain createSsl(Map<String, Object> args, String name);
}
