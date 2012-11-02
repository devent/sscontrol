package com.anrisoftware.sscontrol.workers.api;

import java.io.Serializable;
import java.util.concurrent.Callable;

/**
 * To do work on the server.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
public interface Worker extends Callable<Worker>, Serializable {

	@Override
	Worker call() throws WorkerException;
}
