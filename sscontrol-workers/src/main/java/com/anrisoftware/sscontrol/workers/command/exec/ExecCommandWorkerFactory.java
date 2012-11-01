package com.anrisoftware.sscontrol.workers.command.exec;

import java.util.Map;

/**
 * Factory to create a new worker that executes an external process.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
public interface ExecCommandWorkerFactory {

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
