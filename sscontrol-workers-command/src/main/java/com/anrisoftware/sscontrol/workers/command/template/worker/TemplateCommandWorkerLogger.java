package com.anrisoftware.sscontrol.workers.command.template.worker;

import com.anrisoftware.globalpom.log.AbstractSerializedLogger;
import com.anrisoftware.sscontrol.template.api.TemplateException;
import com.anrisoftware.sscontrol.workers.api.WorkerException;

/**
 * Logging messages for {@link TemplateCommandWorkerLogger}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
class TemplateCommandWorkerLogger extends AbstractSerializedLogger {

	/**
	 * Create logger for {@link TemplateCommandWorker}.
	 */
	public TemplateCommandWorkerLogger() {
		super(TemplateCommandWorker.class);
	}

	WorkerException errorProcessTemplate(TemplateCommandWorker worker,
			TemplateException e) {
		WorkerException ex = new WorkerException("Error processing template", e);
		ex.addContextValue("worker", worker);
		log.error(ex.getLocalizedMessage());
		return ex;
	}
}
