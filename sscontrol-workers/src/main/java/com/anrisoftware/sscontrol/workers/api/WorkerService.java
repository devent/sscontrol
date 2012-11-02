package com.anrisoftware.sscontrol.workers.api;

/**
 * Service that offers the worker.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
public interface WorkerService {

	void setParent(Object injector);

	WorkerServiceInfo getInfo();

	<T extends WorkerFactory> T getWorker();
}
