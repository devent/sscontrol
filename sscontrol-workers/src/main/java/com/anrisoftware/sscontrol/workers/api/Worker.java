package com.anrisoftware.sscontrol.workers.api;

import java.io.Serializable;
import java.util.concurrent.Callable;

public interface Worker extends Callable<Worker>, Serializable {

	@Override
	Worker call() throws WorkerException;
}
