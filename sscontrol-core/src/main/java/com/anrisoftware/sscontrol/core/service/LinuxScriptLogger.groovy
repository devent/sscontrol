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
			log.trace "Installed service packages {} in {}, worker $worker.", packages, script
		} else if (log.debugEnabled) {
			log.debug "Installed service packages {} in {}.", packages, script
		} else {
			log.info "Installed service packages: {}.", packages
		}
	}

	void noConfigurationFound(LinuxScript script, def file) {
		if (log.debugEnabled) {
			log.debug "No configuration file found '{}' in {}.", file, script
		} else {
			log.info "No configuration file found '{}'.", file
		}
	}

	void deployConfigurationDone(LinuxScript script, File file, String configuration) {
		if (log.debugEnabled) {
			log.debug "Deploy configuration to '{}' in {}: '$configuration'.", file, script
		} else {
			log.info "Deploy configuration to '{}'.", file
		}
	}

	void enableRepositoryDone(LinuxScript script, def worker, String distribution, String repository) {
		if (log.traceEnabled) {
			log.trace "Enabled repository $distribution/$repository in {}, worker {}.", script, worker
		} else if (log.debugEnabled) {
			log.debug "Enabled repository $distribution/$repository in {}, worker {}.", script, worker
		} else {
			log.info "Enabled repository {}/{}.", distribution, repository
		}
	}

	void restartServiceDone(LinuxScript script, def worker) {
		if (log.traceEnabled) {
			log.trace "Restarted service {}, worker {}.", script, worker
		} else if (log.debugEnabled) {
			log.debug "Restarted service {}, worker {}.", script, worker
		} else {
			log.info "Restarted service {}.", script.name
		}
	}
}
