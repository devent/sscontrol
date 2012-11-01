package com.anrisoftware.sscontrol.workers.api;

import org.apache.commons.lang3.exception.ContextedException;

public class WorkerException extends ContextedException {

	/**
	 * @version 0.1
	 */
	private static final long serialVersionUID = -5781645443728802699L;

	public WorkerException(String message, Throwable cause) {
		super(message, cause);
	}

	public WorkerException(String message) {
		super(message);
	}

	@Override
	public WorkerException addContextValue(String label, Object value) {
		super.addContextValue(label, value);
		return this;
	}
}
