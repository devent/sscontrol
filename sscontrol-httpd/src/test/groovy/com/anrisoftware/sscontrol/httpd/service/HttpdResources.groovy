package com.anrisoftware.sscontrol.httpd.service

/**
 * Loads the resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class HttpdResources {
	static ubuntu1004Profile = HttpdResources.class.getResource("Ubuntu_10_04Profile.groovy")
	static httpdScript = HttpdResources.class.getResource("Httpd.groovy")
}
