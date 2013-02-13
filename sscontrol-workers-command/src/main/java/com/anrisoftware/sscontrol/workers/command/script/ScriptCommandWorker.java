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
package com.anrisoftware.sscontrol.workers.command.script;

import static com.anrisoftware.sscontrol.workers.command.exec.ExecCommandWorker.DEFAULT_TIMEOUT_MS;
import static java.lang.String.format;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.inject.Named;

import org.apache.commons.exec.Executor;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.propertiesutils.ContextProperties;
import com.anrisoftware.resources.api.ResourcesException;
import com.anrisoftware.resources.templates.api.TemplateResource;
import com.anrisoftware.sscontrol.workers.api.Worker;
import com.anrisoftware.sscontrol.workers.api.WorkerException;
import com.anrisoftware.sscontrol.workers.command.exec.ExecCommandWorker;
import com.anrisoftware.sscontrol.workers.command.exec.ExecCommandWorkerFactory;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * Execute a shell script. The shell script is created from a template.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class ScriptCommandWorker implements Worker {

	/**
	 * @since 1.0
	 */
	private static final long serialVersionUID = 635097928700653033L;

	private final ScriptCommandWorkerLogger log;

	private final ExecCommandWorker commandWorker;

	private final ContextProperties properties;

	/**
	 * Set the template that creates the command to execute.
	 * 
	 * @param logger
	 *            the {@link ScriptCommandWorkerLogger} for logging messages.
	 * 
	 * @param executeFactory
	 *            the {@link ExecCommandWorkerFactory} to create the worker that
	 *            executes the command.
	 * 
	 * @param template
	 *            the template resource {@link TemplatesResources}.
	 * 
	 * @param attributes
	 *            the template attributes.
	 * 
	 * @throws WorkerException
	 *             if there was an error processing the template.
	 */
	@AssistedInject
	ScriptCommandWorker(ScriptCommandWorkerLogger logger,
			ExecCommandWorkerFactory executeFactory,
			@Named("script-command-worker-properties") Properties properties,
			@Assisted TemplateResource template, @Assisted Object... attributes)
			throws WorkerException {
		this(logger, executeFactory, properties, template,
				new HashMap<String, String>(), attributes);
	}

	/**
	 * Set the template that creates the command to execute and the environment
	 * variables for the script.
	 * 
	 * @param logger
	 *            the {@link ScriptCommandWorkerLogger} for logging messages.
	 * 
	 * @param workerFactory
	 *            the {@link ExecCommandWorker} to execute the command.
	 * 
	 * @param template
	 *            the template resource {@link TemplateResource}.
	 * 
	 * @param environment
	 *            a {@link Map} of the environment variables as
	 *            {@code [<name>=<value>]}.
	 * 
	 * @param attributes
	 *            the template attributes.
	 * 
	 * @throws WorkerException
	 *             if there was an error processing the template.
	 */
	@AssistedInject
	ScriptCommandWorker(ScriptCommandWorkerLogger logger,
			ExecCommandWorkerFactory executeFactory,
			@Named("script-command-worker-properties") Properties properties,
			@Assisted TemplateResource template,
			@Assisted Map<String, String> environment,
			@Assisted Object... attributes) throws WorkerException {
		this(logger, executeFactory, properties, template, environment,
				DEFAULT_TIMEOUT_MS, attributes);
	}

	/**
	 * Set the template that creates the command to execute.
	 * 
	 * @param logger
	 *            the {@link ScriptCommandWorkerLogger} for logging messages.
	 * 
	 * @param workerFactory
	 *            the {@link ExecCommandWorker} to execute the command.
	 * 
	 * @param templateFactory
	 *            the {@link TemplateFactory} to process the template.
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
	 * @throws WorkerException
	 *             if there was an error processing the template.
	 */
	@AssistedInject
	ScriptCommandWorker(ScriptCommandWorkerLogger logger,
			ExecCommandWorkerFactory executeFactory,
			@Named("script-command-worker-properties") Properties properties,
			@Assisted TemplateResource template,
			@Assisted Map<String, String> environment,
			@Assisted long timeoutMs, @Assisted Object... attributes)
			throws WorkerException {
		this.log = logger;
		this.properties = new ContextProperties(this, properties);
		this.commandWorker = createCommandWorker(executeFactory, environment,
				timeoutMs, template, attributes);
	}

	private ExecCommandWorker createCommandWorker(
			ExecCommandWorkerFactory executeFactory,
			Map<String, String> environment, long timeoutMs,
			TemplateResource template, Object[] attributes)
			throws WorkerException {
		String shell = properties.getProperty("shell");
		String script = createScript(template, attributes);
		ExecCommandWorker worker = executeFactory.create(
				format("%s \"%s\"", shell, script), environment, timeoutMs);
		worker.setQuotation(false);
		return worker;
	}

	private String createScript(TemplateResource template, Object[] attributes)
			throws WorkerException {
		try {
			return template.getText(true, attributes);
		} catch (ResourcesException e) {
			throw log.errorProcessTemplate(this, e);
		}
	}

	@Override
	public Worker call() throws WorkerException {
		commandWorker.call();
		return this;
	}

	public Map<String, String> getEnvironment() {
		return commandWorker.getEnvironment();
	}

	public void setTimeoutMs(long newTimeoutMs) {
		commandWorker.setTimeoutMs(newTimeoutMs);
	}

	public void setExitValue(int value) {
		commandWorker.setExitValue(value);
	}

	/**
	 * Sets the list of valid exit values for the process.
	 * 
	 * @param values
	 *            the integer array of exit values or {@code null} to skip
	 *            checking of exit codes.
	 * 
	 * @see Executor#setExitValues(int[])
	 */
	public void setExitValues(int[] values) {
		commandWorker.setExitValues(values);
	}

	public int getExitCode() {
		return commandWorker.getExitCode();
	}

	public String getOut() {
		return commandWorker.getOut();
	}

	public String getOut(String charsetName)
			throws UnsupportedEncodingException {
		return commandWorker.getOut(charsetName);
	}

	public String getErr() {
		return commandWorker.getErr();
	}

	public String getErr(String charsetName)
			throws UnsupportedEncodingException {
		return commandWorker.getErr(charsetName);
	}

	@Override
	public String toString() {
		return commandWorker != null ? commandWorker.toString()
				: new ToStringBuilder(this).toString();
	}

}
