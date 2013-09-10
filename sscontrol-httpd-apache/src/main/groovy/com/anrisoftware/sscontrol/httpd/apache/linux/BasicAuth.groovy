package com.anrisoftware.sscontrol.httpd.apache.linux

/**
 * Sets the parent script http/auth.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class BasicAuth {

	ApacheScript script

	void setScript(ApacheScript script) {
		this.script = script
	}

	def propertyMissing(String name) {
		script.getProperty name
	}

	def methodMissing(String name, def args) {
		script.invokeMethod name, args
	}
}
