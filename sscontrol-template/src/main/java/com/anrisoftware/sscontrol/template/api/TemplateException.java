/*
 * Copyright 2012 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-template.
 *
 * sscontrol-template is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-template is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-template. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.template.api;

import org.apache.commons.lang3.exception.ContextedException;

/**
 * Thrown if there was an error processing a template.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
@SuppressWarnings("serial")
public class TemplateException extends ContextedException {

	public TemplateException(String message, Throwable cause) {
		super(message, cause);
	}

	public TemplateException(String message) {
		super(message);
	}

	@Override
	public TemplateException addContextValue(String label, Object value) {
		super.addContextValue(label, value);
		return this;
	}
}
