/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-core.
 *
 * sscontrol-core is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-core is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-core. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.core.service

import com.anrisoftware.globalpom.log.AbstractLogger

/**
 * Logging messages for {@link LinuxScript}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class LinuxScriptLogger extends AbstractLogger {

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
