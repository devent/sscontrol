/*
 * Copyright 2012 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-workers-command.
 *
 * sscontrol-workers-command is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-workers-command is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-workers-command. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.workers.text.match.worker;

import java.io.IOException;

import com.anrisoftware.globalpom.log.AbstractSerializedLogger;
import com.anrisoftware.sscontrol.workers.api.WorkerException;

/**
 * Logging messages for {@link MatchTextWorker}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
class MatchTextWorkerLogger extends AbstractSerializedLogger {

	/**
	 * Create logger for {@link MatchTextWorker}.
	 */
	public MatchTextWorkerLogger() {
		super(MatchTextWorker.class);
	}

	WorkerException readFileError(MatchTextWorker worker, IOException e) {
		WorkerException ex = new WorkerException("Read file error", e);
		ex.addContextValue("worker", worker);
		log.error(ex.getLocalizedMessage());
		return ex;
	}

	void textWasFound(MatchTextWorker worker) {
		log.debug("Search text was found in {}.", worker);
	}

	void textWasNotFound(MatchTextWorker worker) {
		log.debug("Search text was not found in {}.", worker);
	}
}
