/*
 * Copyright 2012 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-api.
 *
 * sscontrol-api is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-api is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-api. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.core.api;

import java.net.URL;
import java.util.Map;

import javax.imageio.spi.ServiceRegistry;

/**
 * Loads a service from a script file.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
public interface ServiceLoader {

	/**
	 * Loads the service from the specified script file URL.
	 * 
	 * @param url
	 *            the {@link URL} of the script file.
	 * 
	 * @param variables
	 *            a {@link Map} of variables that should be injected in the
	 *            script. The map should contain entries
	 *            {@code [<variable name>=<value>, ...]}.
	 * 
	 * @param registry
	 *            the {@link ServicesRegistry} to which to add the service.
	 * 
	 * @param profile
	 *            the {@link ProfileService} or {@code null} if no profile is
	 *            set.
	 * 
	 * @return the {@link ServiceRegistry} that contains the service.
	 * 
	 * @throws NullPointerException
	 *             if the specified script file URL or the specified services
	 *             registry is {@code null}.
	 * 
	 * @throws ServiceException
	 *             if there was an error loading the script file.
	 */
	ServicesRegistry loadService(URL url, Map<String, Object> variables,
			ServicesRegistry registry, ProfileService profile)
			throws ServiceException;
}
