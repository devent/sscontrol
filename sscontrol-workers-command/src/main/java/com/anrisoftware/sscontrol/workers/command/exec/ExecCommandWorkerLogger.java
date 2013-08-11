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
package com.anrisoftware.sscontrol.workers.command.exec;

import java.io.IOException;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.workers.api.WorkerException;

/**
 * Logging messages for {@link ExecCommandWorker}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class ExecCommandWorkerLogger extends AbstractLogger {

	private static final String EXECUTE_COMMAND = "Execute command {} <<<\n{}<<<EOL";
	private static final String FINISH_EXECUTE_COMMAND = "Finished execute command {} code={}, <<<\n{}\n<<<EOL, out= <<<\n{}<<<EOL, err= <<<\n{}<<<EOL";
	private static final String ERROR_EXECUTE_COMMAND_MESSAGE = "Error execute command <<<\n{}\n<<<EOL";
	private static final String WORKER = "worker";
	private static final String ERROR_EXECUTE_COMMAND = "Error execute command";
	private static final String EXIT_CODE = "exit code";
	private static final String OUT = "output";
	private static final String ERR = "error output";
	private static final String COMMAND = "command";

	/**
	 * Create logger for {@link ExecCommandWorker}.
	 */
	public ExecCommandWorkerLogger() {
		super(ExecCommandWorker.class);
	}

	WorkerException errorExecuteCommand(ExecCommandWorker worker, IOException e) {
		return logException(
				new WorkerException(ERROR_EXECUTE_COMMAND, e)
						.add(WORKER, worker).add(COMMAND, worker.getCommand())
						.add(EXIT_CODE, worker.getExitCode())
						.add(OUT, worker.getOut()).add(ERR, worker.getErr()),
				ERROR_EXECUTE_COMMAND_MESSAGE, worker.getCommand());
	}

	void finishedProcess(ExecCommandWorker worker) {
		log.debug(FINISH_EXECUTE_COMMAND, worker, worker.getExitCode(),
				worker.getCommand(), worker.getOut(), worker.getErr());
	}

	void startProcess(ExecCommandWorker worker) {
		log.debug(EXECUTE_COMMAND, worker, worker.getCommand());
	}
}
