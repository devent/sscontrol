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
package com.anrisoftware.sscontrol.workers.text.tokentemplate.worker;

import com.anrisoftware.globalpom.log.AbstractSerializedLogger;

/**
 * Logging messages for {@link TokensTemplateWorker}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class TokensTemplateWorkerLogger extends AbstractSerializedLogger {

	/**
	 * Create logger for {@link TokensTemplateWorker}.
	 */
	public TokensTemplateWorkerLogger() {
		super(TokensTemplateWorker.class);
	}

	void replacedArgument(TokensTemplateWorker worker) {
		log.trace("Replaced argument in {}.", worker);
	}

	void appendArgument(TokensTemplateWorker worker) {
		log.trace("Append argument in {}.", worker);
	}

}
