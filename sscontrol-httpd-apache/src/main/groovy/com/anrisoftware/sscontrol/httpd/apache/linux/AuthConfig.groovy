package com.anrisoftware.sscontrol.httpd.apache.linux

import com.anrisoftware.sscontrol.httpd.statements.auth.AbstractAuth
import com.anrisoftware.sscontrol.httpd.statements.domain.Domain

/**
 * Server authentication configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class AuthConfig {

	ApacheScript script

	def deployAuth(Domain domain, AbstractAuth auth, List serviceConfig) {
		setupDefaultProperties(auth)
		this."enable${auth.type.toString().capitalize()}"()
	}

	private setupDefaultProperties(AbstractAuth auth) {
		if (auth.type == null) {
			auth.type = script.defaultAuthType
		}
		if (auth.provider == null) {
			auth.provider = script.defaultAuthProvider
		}
	}

	private enableBasic() {
	}

	private enableDigest() {
		enableMods "auth_digest"
	}

	def propertyMissing(String name) {
		script.getProperty name
	}

	def methodMissing(String name, def args) {
		script.invokeMethod name, args
	}
}
