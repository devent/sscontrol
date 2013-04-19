/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-workers-text.
 *
 * sscontrol-workers-text is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-workers-text is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-workers-text. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.workers.text.match;

import java.io.IOException;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.workers.api.WorkerException;

/**
 * Logging messages for {@link MatchTextWorker}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class MatchTextWorkerLogger extends AbstractLogger {

	private static final String WORKER = "worker";
	private static final String ERROR_READ_RESOURCE_MESSAGE = "Error read resource '%s'";
	private static final String ERROR_READ_RESOURCE = "Error read resource";
	private static final String SEARCH_TEXT_NOT_FOUND_INFO = "Search text was not found '{}' in {}.";
	private static final String SEARCH_TEXT_NOT_FOUND = "Search text was not found in {}.";
	private static final String SEARCH_TEXT_FOUND_INFO = "Search text was found '{}' in {}.";
	private static final String SEARCH_TEXT_FOUND = "Search text was found in {}.";

	/**
	 * Create logger for {@link MatchTextWorker}.
	 */
	public MatchTextWorkerLogger() {
		super(MatchTextWorker.class);
	}

	WorkerException readFileError(MatchTextWorker worker, IOException e) {
		return logException(
				new WorkerException(ERROR_READ_RESOURCE, e).addContextValue(
						WORKER, worker), ERROR_READ_RESOURCE_MESSAGE,
				worker.getResource());
	}

	void textWasFound(MatchTextWorker worker) {
		if (log.isDebugEnabled()) {
			log.debug(SEARCH_TEXT_FOUND, worker);
		} else {
			log.info(SEARCH_TEXT_FOUND_INFO, worker.getPattern(),
					worker.getResource());
		}
	}

	void textWasNotFound(MatchTextWorker worker) {
		if (log.isDebugEnabled()) {
			log.debug(SEARCH_TEXT_NOT_FOUND, worker);
		} else {
			log.info(SEARCH_TEXT_NOT_FOUND_INFO, worker.getPattern(),
					worker.getResource());
		}
	}
}
