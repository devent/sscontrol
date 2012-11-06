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
package com.anrisoftware.sscontrol.workers.command.script.worker;

import java.net.URL;
import java.util.Map;

import com.anrisoftware.sscontrol.workers.api.WorkerException;
import com.anrisoftware.sscontrol.workers.api.WorkerFactory;
import com.anrisoftware.sscontrol.workers.command.exec.worker.ExecCommandWorker;
import com.google.inject.assistedinject.Assisted;

/**
 * Factory to create a new worker that execute a shell script. The shell script
 * is created from a template.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
public interface ScriptCommandWorkerFactory extends WorkerFactory {

	/**
	 * Creates a new worker that executes a shell script. The shell script is
	 * created from a template. The process will be terminated after
	 * {@link ExecCommandWorker#DEFAULT_TIMEOUT_MS}.
	 * 
	 * @param templateResource
	 *            the {@link URL} of the template.
	 * 
	 * @param templateName
	 *            the template name.
	 * 
	 * @param attributes
	 *            a {@link Map} of the template attributes.
	 * 
	 * @return the {@link ScriptCommandWorker}.
	 * 
	 * @throws WorkerException
	 *             if there was an error processing the template.
	 */
	ScriptCommandWorker create(URL templateResource, String templateName,
			Map<String, Object> attributes) throws WorkerException;

	/**
	 * Creates a new worker that executes a shell script. The shell script is
	 * created from a template and is executed with the specified environment
	 * variables. The process will be terminated after
	 * {@link ExecCommandWorker#DEFAULT_TIMEOUT_MS}.
	 * 
	 * @param templateResource
	 *            the {@link URL} of the template.
	 * 
	 * @param templateName
	 *            the template name.
	 * 
	 * @param attributes
	 *            a {@link Map} of the template attributes.
	 * 
	 * @param environment
	 *            a {@link Map} of the environment variables as
	 *            {@code [<name>=<value>]}.
	 * 
	 * @return the {@link ScriptCommandWorker}.
	 * 
	 * @throws WorkerException
	 *             if there was an error processing the template.
	 */
	ScriptCommandWorker create(URL templateResource, String templateName,
			Map<String, Object> attributes,
			@Assisted("environment") Map<String, String> environment)
			throws WorkerException;

	/**
	 * Creates a new worker that executes a shell script. The shell script is
	 * created from a template and is executed with the specified environment
	 * variables. The process will be terminated after the specified timeout.
	 * 
	 * @param templateResource
	 *            the {@link URL} of the template.
	 * 
	 * @param templateName
	 *            the template name.
	 * 
	 * @param attributes
	 *            a {@link Map} of the template attributes.
	 * 
	 * @param environment
	 *            a {@link Map} of the environment variables as
	 *            {@code [<name>=<value>]}.
	 * 
	 * @param timeoutMs
	 *            the timeout in milliseconds.
	 * 
	 * @return the {@link ScriptCommandWorker}.
	 * 
	 * @throws WorkerException
	 *             if there was an error processing the template.
	 */
	ScriptCommandWorker create(URL templateResource, String templateName,
			Map<String, Object> attributes,
			@Assisted("environment") Map<String, String> environment,
			long timeoutMs) throws WorkerException;
}
