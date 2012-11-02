package com.anrisoftware.sscontrol.workers.command.template.worker;

import java.net.URL;
import java.util.Map;

import com.anrisoftware.sscontrol.workers.api.WorkerException;
import com.anrisoftware.sscontrol.workers.api.WorkerFactory;
import com.anrisoftware.sscontrol.workers.command.exec.worker.ExecCommandWorker;
import com.google.inject.assistedinject.Assisted;

/**
 * Factory to create a new worker that executes an external process.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
public interface TemplateCommandWorkerFactory extends WorkerFactory {

	/**
	 * Creates a new worker that executes an external process from the specified
	 * command. The command is created from a template. The process will be
	 * terminated after {@link ExecCommandWorker#DEFAULT_TIMEOUT_MS}.
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
	 * @return the {@link TemplateCommandWorker}.
	 * 
	 * @throws WorkerException
	 *             if there was an error processing the template.
	 */
	TemplateCommandWorker create(URL templateResource, String templateName,
			Map<String, Object> attributes) throws WorkerException;

	/**
	 * Creates a new worker that executes an external process from the specified
	 * command and with the specified environment variables. The command is
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
	 * @param environment
	 *            a {@link Map} of the environment variables as
	 *            {@code [<name>=<value>]}.
	 * 
	 * @return the {@link TemplateCommandWorker}.
	 * 
	 * @throws WorkerException
	 *             if there was an error processing the template.
	 */
	TemplateCommandWorker create(URL templateResource, String templateName,
			Map<String, Object> attributes,
			@Assisted("environment") Map<String, String> environment)
			throws WorkerException;

	/**
	 * Creates a new worker that executes an external process from the specified
	 * command and with the specified environment variables. The command is
	 * created from a template. The process will be terminated after the
	 * specified timeout.
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
	 * @return the {@link TemplateCommandWorker}.
	 * 
	 * @throws WorkerException
	 *             if there was an error processing the template.
	 */
	TemplateCommandWorker create(URL templateResource, String templateName,
			Map<String, Object> attributes,
			@Assisted("environment") Map<String, String> environment,
			long timeoutMs) throws WorkerException;
}
