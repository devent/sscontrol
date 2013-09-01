package com.anrisoftware.sscontrol.httpd.statements.auth;

/**
 * Different authentication types.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public enum AuthType {

	/**
	 * MD5 digest authentication.
	 */
	digest,

	/**
	 * HTTP basic authentication.
	 */
	basic
}
