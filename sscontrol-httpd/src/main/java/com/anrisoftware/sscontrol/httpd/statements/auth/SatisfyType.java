package com.anrisoftware.sscontrol.httpd.statements.auth;

/**
 * How to set the criteria that must be to allow access to a resource.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public enum SatisfyType {

	/**
	 * All criteria must be met.
	 */
	all,

	/**
	 * Only one criteria must be met.
	 */
	any
}
