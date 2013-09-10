package com.anrisoftware.sscontrol.httpd.apache.linux

import com.anrisoftware.sscontrol.httpd.statements.auth.AbstractAuth
import com.anrisoftware.sscontrol.httpd.statements.domain.Domain

/**
 * Sets the default properties for http/auth.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class BasicAuth {

	ApacheScript script

	void setScript(ApacheScript script) {
		this.script = script
	}

	void deployAuth(Domain domain, AbstractAuth auth, List serviceConfig) {
		setupDefaultProperties(auth)
	}

	private setupDefaultProperties(AbstractAuth auth) {
		if (auth.type == null) {
			auth.type = script.defaultAuthType
		}
		if (auth.provider == null) {
			auth.provider = script.defaultAuthProvider
		}
	}

	def propertyMissing(String name) {
		script.getProperty name
	}

	def methodMissing(String name, def args) {
		script.invokeMethod name, args
	}
}
