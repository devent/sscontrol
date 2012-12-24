package com.anrisoftware.sscontrol.dns.maradns.linux

import com.anrisoftware.globalpom.log.AbstractSerializedLogger
import com.anrisoftware.sscontrol.core.api.ProfileProperties
import com.anrisoftware.sscontrol.workers.command.script.ScriptCommandWorker

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

	void installPackagesDone(LinuxScript script, ScriptCommandWorker worker, ProfileProperties profile) {
		if (log.isTraceEnabled()) {
			log.trace "Installed dns service packages for {}: {}, output: '${worker.out}', error: '${worker.err}', exit code: '${worker.exitCode}'.",
							script, worker
		} else if (log.isDebugEnabled()) {
			log.debug "Installed dns service packages for {}: {}.", script, worker
		} else {
			log.info "Installed dns service packages: {}.", profile.packages
		}
	}

	void noMararcConfigurationFound(LinuxScript script, File file) {
		if (log.isDebugEnabled()) {
			log.warn "No MaraDNS configuration file found for {}: {}.", script, file
		} else {
			log.warn "No MaraDNS configuration file found: {}.", script, file
		}
	}

	void deployConfiguration(LinuxScript script) {
		if (log.isDebugEnabled()) {
			log.debug "Deployed MaraDNS configuration for {}.", script
		} else {
			log.info "Deployed MaraDNS configuration."
		}
	}
}
