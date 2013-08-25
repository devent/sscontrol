package com.anrisoftware.sscontrol.httpd.statements;

/**
 * Factory to create redirects.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface RedirectFactory {

	/**
	 * Creates the redirect to www.
	 */
	RedirectToWww createToWww();

	/**
	 * Creates the redirect from http to https.
	 */
	RedirectHttpToHttps createHttpToHttps();
}
