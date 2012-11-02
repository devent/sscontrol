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

import java.net.URL;
import java.util.Properties;

/**
 * Factory to create a new template.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
public interface TemplateFactory {

	/**
	 * Creates a new template from the specified resource URL and properties.
	 * 
	 * @param resource
	 *            the {@link URL} of the template file.
	 * 
	 * @param properties
	 *            the {@link Properties} for the template file.
	 * 
	 * @return the {@link TemplateWorker}. if there was an error creating the
	 *         template.
	 */
	Template create(URL resource, Properties properties)
			throws TemplateException;

	/**
	 * Creates a new template from the specified resource URL using the default
	 * properties.
	 * 
	 * @param resource
	 *            the {@link URL} of the template file.
	 * 
	 * @return the {@link TemplateWorker}. if there was an error creating the
	 *         template.
	 */
	Template create(URL resource) throws TemplateException;
}
