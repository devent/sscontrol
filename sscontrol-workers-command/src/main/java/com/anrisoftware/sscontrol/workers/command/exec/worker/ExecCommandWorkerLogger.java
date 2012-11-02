package com.anrisoftware.sscontrol.workers.command.exec.worker;

import java.io.IOException;

import com.anrisoftware.globalpom.log.AbstractSerializedLogger;
import com.anrisoftware.sscontrol.workers.api.WorkerException;

/**
 * Logging messages for {@link ExecCommandWorker}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
class ExecCommandWorkerLogger extends AbstractSerializedLogger {

	/**
	 * Create logger for {@link ExecCommandWorker}.
	 */
	public ExecCommandWorkerLogger() {
		super(ExecCommandWorker.class);
	}

	WorkerException errorExecuteCommand(ExecCommandWorker worker, IOException e) {
		WorkerException ex = new WorkerException(
				"Error execute command in worker", e);
		ex.addContextValue("worker", worker);
		log.error(ex.getLocalizedMessage());
		return ex;
	}

	void finishedProcess(ExecCommandWorker worker) {
		log.trace("Finished worker {}.", worker);
	}
}
