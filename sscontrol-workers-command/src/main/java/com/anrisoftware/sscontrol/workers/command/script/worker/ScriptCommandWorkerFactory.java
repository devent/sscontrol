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

import java.util.Map;

import com.anrisoftware.resources.templates.api.TemplateResource;
import com.anrisoftware.sscontrol.workers.api.WorkerException;
import com.anrisoftware.sscontrol.workers.api.WorkerFactory;
import com.anrisoftware.sscontrol.workers.command.exec.worker.ExecCommandWorker;

/**
 * Factory to create a new worker that execute a shell script. The shell script
 * is created from a template.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface ScriptCommandWorkerFactory extends WorkerFactory {

	/**
	 * Creates a new worker that executes a shell script. The shell script is
	 * created from a template. The process will be terminated after
	 * {@link ExecCommandWorker#DEFAULT_TIMEOUT_MS}.
	 * 
	 * @param template
	 *            the {@link TemplateResource} template resource.
	 * 
	 * @param attributes
	 *            the template attributes.
	 * 
	 * @return the {@link ScriptCommandWorker}.
	 * 
	 * @throws WorkerException
	 *             if there was an error processing the template.
	 */
	ScriptCommandWorker create(TemplateResource template, Object... attributes)
			throws WorkerException;

	/**
	 * Creates a new worker that executes a shell script. The shell script is
	 * created from a template and is executed with the specified environment
	 * variables. The process will be terminated after
	 * {@link ExecCommandWorker#DEFAULT_TIMEOUT_MS}.
	 * 
	 * @param template
	 *            the {@link TemplateResource} template resource.
	 * 
	 * @param environment
	 *            a {@link Map} of the environment variables as
	 *            {@code [<name>=<value>]}.
	 * 
	 * @param attributes
	 *            the template attributes.
	 * 
	 * @return the {@link ScriptCommandWorker}.
	 * 
	 * @throws WorkerException
	 *             if there was an error processing the template.
	 */
	ScriptCommandWorker create(TemplateResource template,
			Map<String, String> environment, Object... attributes)
			throws WorkerException;

	/**
	 * Creates a new worker that executes a shell script. The shell script is
	 * created from a template and is executed with the specified environment
	 * variables. The process will be terminated after the specified timeout.
	 * 
	 * @param template
	 *            the {@link TemplateResource} that returns the template.
	 * 
	 * @param environment
	 *            a {@link Map} of the environment variables as
	 *            {@code [<name>=<value>]}.
	 * 
	 * @param timeoutMs
	 *            the timeout in milliseconds.
	 * 
	 * @param attributes
	 *            the template attributes.
	 * 
	 * @return the {@link ScriptCommandWorker}.
	 * 
	 * @throws WorkerException
	 *             if there was an error processing the template.
	 */
	ScriptCommandWorker create(TemplateResource template,
			Map<String, String> environment, long timeoutMs,
			Object... attributes) throws WorkerException;
}
