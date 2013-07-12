/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
 * 
 * This file is part of sscontrol-api.
 * 
 * sscontrol-api is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * sscontrol-api is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-api. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.core.api;

import java.util.Map;

import com.anrisoftware.globalpom.exceptions.Context;

/**
 * Indicates an error with a service.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
@SuppressWarnings("serial")
public class ServiceException extends Exception {

	private final Context<ServiceException> context;

	/**
	 * Sets the message and the cause of the exception.
	 * 
	 * @param message
	 *            the message.
	 * 
	 * @param cause
	 *            the {@link Throwable} cause.
	 */
	public ServiceException(String message, Throwable cause) {
		super(message, cause);
		this.context = new Context<ServiceException>(this);
	}

	/**
	 * Sets the message of the exception.
	 * 
	 * @param message
	 *            the message.
	 * 
	 */
	public ServiceException(String message) {
		super(message);
		this.context = new Context<ServiceException>(this);
	}

	/**
	 * @see Context#addContext(String, Object)
	 */
	public ServiceException add(String name, Object value) {
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
