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

import java.io.Serializable;

/**
 * Process a template with attributes.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
public interface Template extends Serializable {

	/**
	 * Process the template with the specified name and the attributes.
	 * 
	 * @param name
	 *            the name of the template.
	 * 
	 * @param data
	 *            the attributes for the template. Expecting first the attribute
	 *            name and then the attribute value.
	 * 
	 * @return the processed template string.
	 * 
	 * @throws TemplateException
	 *             if there was an error processing a template.
	 */
	String process(String name, Object... data) throws TemplateException;

}
