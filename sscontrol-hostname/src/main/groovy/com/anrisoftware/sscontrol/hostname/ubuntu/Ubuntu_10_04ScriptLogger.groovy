/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-hostname.
 *
 * sscontrol-hostname is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-hostname is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-hostname. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.hostname.ubuntu

import javax.inject.Singleton

import com.anrisoftware.globalpom.log.AbstractLogger
import com.anrisoftware.sscontrol.workers.command.script.ScriptCommandWorker

/**
 * Logging messages for {@link Ubuntu_10_04Script}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class Ubuntu_10_04ScriptLogger extends AbstractLogger {

	/**
	 * Creates a logger for {@link Ubuntu_10_04ScriptLogger}.
	 */
	public Ubuntu_10_04ScriptLogger() {
		super(Ubuntu_10_04Script.class)
	}

	void restartServiceDone(Ubuntu_10_04Script script, ScriptCommandWorker worker) {
		if (log.debugEnabled) {
			log.debug "Restarted service {}, worker {}.", script, worker
		} else {
			log.info "Restarted service {}.", script.name
		}
	}
}

