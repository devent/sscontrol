package com.anrisoftware.sscontrol.httpd.apache.linux

import com.anrisoftware.sscontrol.httpd.statements.domain.Domain
import com.anrisoftware.sscontrol.httpd.statements.phpmyadmin.PhpmyadminService

class PhpmyadminConfig {

	ApacheScript script

	def deployService(Domain domain, PhpmyadminService service) {
		installPackages phpmyadminPackages
	}

	/**
	 * Returns the list of needed packages for phpmyadmin.
	 *
	 * <ul>
	 * <li>profile property {@code "phpmyadmin_packages"}</li>
	 * </ul>
	 */
	List getPhpmyadminPackages() {
		profileListProperty "phpmyadmin_packages", defaultProperties
	}

	def propertyMissing(String name) {
		script.getProperty name
	}

	def methodMissing(String name, def args) {
		script.invokeMethod name, args
	}
}
