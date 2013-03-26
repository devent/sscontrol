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
import java.util.concurrent.Callable;

/**
 * Configure a service on the target server.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
public interface Service extends Callable<Service>, Serializable {

	/**
	 * Returns the service name. Examples are: profile, http, mta, hosts,
	 * hostname, dns, etc.
	 * 
	 * @return the name of the service.
	 */
	String getName();

	/**
	 * Configure the service on the target server.
	 * 
	 * @throws ServiceException
	 *             if there was an error with the configuration.
	 * 
	 * @return this {@link Service}.
	 */
	@Override
	Service call() throws ServiceException;
}
