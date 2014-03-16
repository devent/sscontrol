/*
 * Copyright 2012-2014 Erwin Müller <erwin.mueller@deventm.org>
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

import java.util.Map;

/**
 * Factory to create the service loader.
 * 
 * @see ServiceLoader
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface ServiceLoaderFactory {

    /**
     * Creates the service loader.
     * 
     * @param registry
     *            the {@link ServicesRegistry} to which to add the service.
     * 
     * @param variables
     *            a {@link Map} of variables that should be injected in the
     *            script. The map should contain entries
     *            {@code [<variable name>=<value>, ...]}.
     * 
     * @return the {@link ServiceLoader}.
     */
    ServiceLoader create(ServicesRegistry registry,
            Map<String, Object> variables);
}
