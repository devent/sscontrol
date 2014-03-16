/*
 * Copyright 2012-2014 Erwin Müller <erwin.mueller@deventm.org>
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

import java.util.Map;

import com.anrisoftware.sscontrol.workers.api.WorkerFactory;

/**
 * Factory to create a new worker that executes an external process.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface ExecCommandWorkerFactory extends WorkerFactory {

	/**
	 * Creates a new worker that executes an external process from the specified
	 * command. The process will be terminated after
	 * {@link ExecCommandWorker#DEFAULT_TIMEOUT_MS}.
	 * 
	 * @param command
	 *            the command string with arguments.
	 * 
	 * @return the {@link ExecCommandWorker}.
	 */
	ExecCommandWorker create(String command);

	/**
	 * Creates a new worker that executes an external process from the specified
	 * command and with the specified environment variables. The process will be
	 * terminated after {@link ExecCommandWorker#DEFAULT_TIMEOUT_MS}.
	 * 
	 * @param command
	 *            the command string with arguments.
	 * 
	 * @param environment
	 *            a {@link Map} of the environment variables as
	 *            {@code [<name>=<value>]}.
	 * 
	 * @return the {@link ExecCommandWorker}.
	 */
	ExecCommandWorker create(String command, Map<String, String> environment);

	/**
	 * Creates a new worker that executes an external process from the specified
	 * command and with the specified environment variables. The process will be
	 * terminated after the specified timeout.
	 * 
	 * @param command
	 *            the command string with arguments.
	 * 
	 * @param environment
	 *            a {@link Map} of the environment variables as
	 *            {@code [<name>=<value>]}.
	 * 
	 * @param timeoutMs
	 *            the timeout in milliseconds.
	 * 
	 * @return the {@link ExecCommandWorker}.
	 */
	ExecCommandWorker create(String command, Map<String, String> environment,
			long timeoutMs);
}
