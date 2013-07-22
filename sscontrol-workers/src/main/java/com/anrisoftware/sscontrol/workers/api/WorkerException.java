/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
 * 
 * This file is part of sscontrol-workers.
 * 
 * sscontrol-workers is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * sscontrol-workers is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-workers. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.workers.api;

import java.util.Map;

import com.anrisoftware.globalpom.exceptions.Context;

/**
 * Indicate that some error occured while executing a worker.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class WorkerException extends Exception {

	private final Context<WorkerException> context;

	public WorkerException(String message, Throwable cause) {
		super(message, cause);
		this.context = new Context<WorkerException>(this);
	}

	public WorkerException(String message) {
		super(message);
		this.context = new Context<WorkerException>(this);
	}

	/**
	 * @see Context#addContext(String, Object)
	 */
	public WorkerException add(String name, Object value) {
		context.addContext(name, value);
		return this;
	}

	/**
	 * @see Context#getContext()
	 */
	public Map<String, Object> getContext() {
		return context.getContext();
	}

	@Override
	public String toString() {
		return context.toString();
	}

}
