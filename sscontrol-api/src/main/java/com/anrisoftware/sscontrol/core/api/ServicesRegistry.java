/*
 * Copyright 2012-2015 Erwin Müller <erwin.mueller@deventm.org>
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

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Global registry of known services.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
public interface ServicesRegistry extends Serializable {

	/**
	 * Adds a service to the registry. It is possible to add multiple services
	 * with the same name.
	 * 
	 * @param service
	 *            the {@link Service} to add.
	 * 
	 * @throws NullPointerException
	 *             if the specified service is {@code null}.
	 */
	void addService(Service service);

	/**
	 * Returns the services with the specified name.
	 * 
	 * @param name
	 *            the name of the services.
	 * 
	 * @return an unmodifiable {@link List} of {@link Service}s with the
	 *         specified name.
	 */
	List<Service> getService(String name);

	/**
	 * Returns the service names in this registry.
	 * 
	 * @return an unmodifiable {@link Set} of the names of the services.
	 */
	Set<String> getServiceNames();

	/**
	 * Returns all services in this registry.
	 * 
	 * @return an unmodifiable {@link Collection} of all {@link Service}s.
	 */
	Collection<Service> getAllServices();

	/**
	 * Tests if the registry contains the services with the specified name.
	 * 
	 * @param name
	 *            the name of the services.
	 * 
	 * @return {@code true} if the registry contains the services with the
	 *         specified name.
	 */
	boolean haveService(String name);
}
