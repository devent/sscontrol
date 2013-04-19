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
package com.anrisoftware.sscontrol.workers.text.tokentemplate;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link TokensTemplateWorker}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class TokensTemplateWorkerLogger extends AbstractLogger {

	private static final String APPEND_ARGUMENT_INFO = "Append argument in template {}.";
	private static final String APPEND_ARGUMENT = "Append argument in {}.";
	private static final String REPLACED_ARGUMENT_INFO = "Replaced argument in template {}.";
	private static final String REPLACED_ARGUMENT = "Replaced argument in {}.";

	/**
	 * Create logger for {@link TokensTemplateWorker}.
	 */
	public TokensTemplateWorkerLogger() {
		super(TokensTemplateWorker.class);
	}

	void replacedArgument(TokensTemplateWorker worker) {
		if (log.isDebugEnabled()) {
			log.debug(REPLACED_ARGUMENT, worker);
		} else {
			log.info(REPLACED_ARGUMENT_INFO, worker.getTemplate());
		}
	}

	void appendArgument(TokensTemplateWorker worker) {
		if (log.isDebugEnabled()) {
			log.debug(APPEND_ARGUMENT, worker);
		} else {
			log.info(APPEND_ARGUMENT_INFO, worker.getTemplate());
		}
	}

}
