/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.workers.command.script;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.resources.api.ResourcesException;
import com.anrisoftware.sscontrol.workers.api.WorkerException;

/**
 * Logging messages for {@link ScriptCommandWorkerLogger}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class ScriptCommandWorkerLogger extends AbstractLogger {

	private static final String WORKER = "worker";
	private static final String ERROR_PROCESSING2 = "Error processing template {}#{}.";
	private static final String ERROR_PROCESSING = "Error processing template";

	/**
	 * Create logger for {@link ScriptCommandWorker}.
	 */
	public ScriptCommandWorkerLogger() {
		super(ScriptCommandWorker.class);
	}

	WorkerException errorProcessTemplate(ScriptCommandWorker worker,
			ResourcesException e) {
		return logException(
				new WorkerException(ERROR_PROCESSING, e).add(WORKER, worker),
				ERROR_PROCESSING2, e.getClassName(), e.getKey());
	}
}
