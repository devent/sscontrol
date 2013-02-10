package com.anrisoftware.sscontrol.core.service

import com.anrisoftware.globalpom.log.AbstractSerializedLogger

/**
 * Logging messages for {@link LinuxScript}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class LinuxScriptLogger extends AbstractSerializedLogger {

	/**
	 * Create logger for {@link LinuxScript}.
	 */
	LinuxScriptLogger() {
		super(LinuxScript.class)
	}

	void installPackagesDone(LinuxScript script, def worker, def packages) {
		if (log.traceEnabled) {
			log.trace "Installed dhclient service packages {} in {}, worker $worker.", this, packages
		} else if (log.debugEnabled) {
			log.debug "Installed dhclient service packages {} in {}.", this, packages
		} else {
			log.info "Installed dhclient service packages: {}.", packages
		}
	}

	void noConfigurationFound(LinuxScript script, def file) {
		if (log.debugEnabled) {
			log.debug "No configuration file found '{}' in {}.", file, this
		} else {
			log.info "No configuration file found '{}'.", file
		}
	}

	void deployConfigurationDone(LinuxScript script, File file, String configuration) {
		if (log.debugEnabled) {
			log.debug "Deploy configuration to '{}' in {}: '$configuration'.", file, this
		} else {
			log.info "Deploy configuration to '{}'.", file
		}
	}
}
