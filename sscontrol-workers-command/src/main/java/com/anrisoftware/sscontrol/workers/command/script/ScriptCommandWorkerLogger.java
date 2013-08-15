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

import java.io.IOException;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.resources.api.ResourcesException;
import com.anrisoftware.sscontrol.workers.api.WorkerException;

/**
 * Logging messages for {@link ScriptCommandWorkerLogger}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class ScriptCommandWorkerLogger extends AbstractLogger {

	private static final String WORKER = "worker";
	private static final String ERROR_PROCESSING2 = "Error processing template {}#{}.";
	private static final String ERROR_PROCESSING = "Error processing template";
	private static final String ERROR_COPY = "Error copy script";
	private static final String ERROR_COPY2 = "Error copy script <<<\n{}\n<<<EOL";
	private static final String EXECUTE_COMMAND = "Execute script {}: '{}' <<<\n{}<<<EOL";
	private static final String FINISH_EXECUTE_COMMAND = "Finished execute script {}: '{}' <<<\n{}\n<<<EOL";

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

	WorkerException errorCopyScript(ScriptCommandWorker worker, IOException e) {
		return logException(
				new WorkerException(ERROR_COPY, e).add(WORKER, worker),
				ERROR_COPY2, worker.getCommand());
	}

	void finishedScript(ScriptCommandWorker worker) {
		log.debug(FINISH_EXECUTE_COMMAND, worker, worker.getScriptFile(),
				worker.getCommand());
	}

	void startScript(ScriptCommandWorker worker) {
		log.debug(EXECUTE_COMMAND, worker, worker.getScriptFile(),
				worker.getCommand());
	}
}
