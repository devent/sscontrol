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

import static com.anrisoftware.sscontrol.workers.command.exec.worker.ExecCommandWorker.DEFAULT_TIMEOUT_MS;
import static java.lang.String.format;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.inject.Named;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.propertiesutils.ContextProperties;
import com.anrisoftware.sscontrol.template.api.Template;
import com.anrisoftware.sscontrol.template.api.TemplateException;
import com.anrisoftware.sscontrol.template.api.TemplateFactory;
import com.anrisoftware.sscontrol.workers.api.Worker;
import com.anrisoftware.sscontrol.workers.api.WorkerException;
import com.anrisoftware.sscontrol.workers.command.exec.worker.ExecCommandWorker;
import com.anrisoftware.sscontrol.workers.command.exec.worker.ExecCommandWorkerFactory;
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
			TemplateFactory templateFactory,
			@Named("script-command-worker-properties") Properties properties,
			@Assisted URL templateResource, @Assisted String templateName,
			@Assisted Map<String, Object> attributes) throws WorkerException {
		this(logger, executeFactory, templateFactory, properties,
				templateResource, templateName, attributes,
				new HashMap<String, String>());
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
	 * @param environment
	 *            a {@link Map} of the environment variables as
	 *            {@code [<name>=<value>]}.
	 * 
	 * @throws WorkerException
	 *             if there was an error processing the template.
	 */
	@AssistedInject
	ScriptCommandWorker(ScriptCommandWorkerLogger logger,
			ExecCommandWorkerFactory executeFactory,
			TemplateFactory templateFactory,
			@Named("script-command-worker-properties") Properties properties,
			@Assisted URL templateResource, @Assisted String templateName,
			@Assisted Map<String, Object> attributes,
			@Assisted("environment") Map<String, String> environment)
			throws WorkerException {
		this(logger, executeFactory, templateFactory, properties,
				templateResource, templateName, attributes, environment,
				DEFAULT_TIMEOUT_MS);
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
			TemplateFactory templateFactory,
			@Named("script-command-worker-properties") Properties properties,
			@Assisted URL templateResource, @Assisted String templateName,
			@Assisted Map<String, Object> attributes,
			@Assisted("environment") Map<String, String> environment,
			@Assisted long timeoutMs) throws WorkerException {
		this.log = logger;
		this.properties = new ContextProperties(this, properties);
		this.commandWorker = createCommandWorker(executeFactory, environment,
				timeoutMs, templateFactory, templateResource, templateName,
				attributes);
	}

	private ExecCommandWorker createCommandWorker(
			ExecCommandWorkerFactory executeFactory,
			Map<String, String> environment, long timeoutMs,
			TemplateFactory templateFactory, URL templateResource,
			String templateName, Map<String, Object> attributes)
			throws WorkerException {
		String shell = properties.getProperty("shell");
		String script = createScript(templateFactory, templateResource,
				templateName, attributes);
		ExecCommandWorker worker = executeFactory.create(
				format("%s \"%s\"", shell, script), environment, timeoutMs);
		worker.setQuotation(false);
		return worker;
	}

	private String createScript(TemplateFactory factory, URL templateResource,
			String templateName, Map<String, Object> attributes)
			throws WorkerException {
		try {
			Template template = factory.create(templateResource);
			return template.process(templateName, toArray(attributes));
		} catch (TemplateException e) {
			throw log.errorProcessTemplate(this, e);
		}
	}

	private Object[] toArray(Map<String, Object> attributes) {
		Object[] attr = new Object[attributes.size() * 2];
		int i = 0;
		for (Map.Entry<String, Object> entry : attributes.entrySet()) {
			attr[i++] = entry.getKey();
			attr[i++] = entry.getValue();
		}
		return attr;
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
