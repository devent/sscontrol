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

import org.apache.commons.lang3.exception.ContextedException;

/**
 * Indicate that some error occured while executing a worker.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
@SuppressWarnings("serial")
public class WorkerException extends ContextedException {

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
