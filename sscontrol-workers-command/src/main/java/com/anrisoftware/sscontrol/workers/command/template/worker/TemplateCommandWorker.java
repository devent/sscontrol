package com.anrisoftware.sscontrol.workers.command.template.worker;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Map;

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
 * Execute a command that was created from a template.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
public class TemplateCommandWorker implements Worker {

	/**
	 * @version 0.1
	 */
	private static final long serialVersionUID = 635097928700653033L;

	private final TemplateCommandWorkerLogger log;

	private final ExecCommandWorker executeWorker;

	/**
	 * Set the template that creates the command to execute.
	 * 
	 * @param logger
	 *            the {@link TemplateCommandWorkerLogger} for logging messages.
	 * 
	 * @param executeFactory
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
	TemplateCommandWorker(TemplateCommandWorkerLogger logger,
			ExecCommandWorkerFactory executeFactory,
			TemplateFactory templateFactory, @Assisted URL templateResource,
			@Assisted String templateName,
			@Assisted Map<String, Object> attributes) throws WorkerException {
		this.log = logger;
		this.executeWorker = executeFactory.create(createCommand(
				templateFactory, templateResource, templateName, attributes));
	}

	/**
	 * Set the template that creates the command to execute. Sets the specified
	 * environment for the process.
	 * 
	 * @param logger
	 *            the {@link TemplateCommandWorkerLogger} for logging messages.
	 * 
	 * @param executeFactory
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
	TemplateCommandWorker(TemplateCommandWorkerLogger logger,
			ExecCommandWorkerFactory executeFactory,
			TemplateFactory templateFactory, @Assisted URL templateResource,
			@Assisted String templateName,
			@Assisted Map<String, Object> attributes,
			@Assisted("environment") Map<String, String> environment)
			throws WorkerException {
		this.log = logger;
		this.executeWorker = executeFactory.create(
				createCommand(templateFactory, templateResource, templateName,
						attributes), environment);
	}

	/**
	 * Set the template that creates the command to execute. Sets the specified
	 * environment and the timeout for the process.
	 * 
	 * @param logger
	 *            the {@link TemplateCommandWorkerLogger} for logging messages.
	 * 
	 * @param executeFactory
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
	 * @param timeoutMs
	 *            the timeout in milliseconds.
	 * 
	 * @throws WorkerException
	 *             if there was an error processing the template.
	 */
	@AssistedInject
	TemplateCommandWorker(TemplateCommandWorkerLogger logger,
			ExecCommandWorkerFactory executeFactory,
			TemplateFactory templateFactory, @Assisted URL templateResource,
			@Assisted String templateName,
			@Assisted Map<String, Object> attributes,
			@Assisted("environment") Map<String, String> environment,
			@Assisted long timeoutMs) throws WorkerException {
		this.log = logger;
		this.executeWorker = executeFactory.create(
				createCommand(templateFactory, templateResource, templateName,
						attributes), environment, timeoutMs);
	}

	private String createCommand(TemplateFactory factory, URL templateResource,
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
		executeWorker.call();
		return this;
	}

	public Map<String, String> getEnvironment() {
		return executeWorker.getEnvironment();
	}

	public void setTimeoutMs(long newTimeoutMs) {
		executeWorker.setTimeoutMs(newTimeoutMs);
	}

	public void setExitValue(int value) {
		executeWorker.setExitValue(value);
	}

	public int getExitCode() {
		return executeWorker.getExitCode();
	}

	public String getOut() {
		return executeWorker.getOut();
	}

	public String getOut(String charsetName)
			throws UnsupportedEncodingException {
		return executeWorker.getOut(charsetName);
	}

	public String getErr() {
		return executeWorker.getErr();
	}

	public String getErr(String charsetName)
			throws UnsupportedEncodingException {
		return executeWorker.getErr(charsetName);
	}

	@Override
	public String toString() {
		return executeWorker.toString();
	}

}
