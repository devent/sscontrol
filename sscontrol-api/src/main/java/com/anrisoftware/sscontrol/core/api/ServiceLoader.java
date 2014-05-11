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

import java.net.URL;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import javax.imageio.spi.ServiceRegistry;

/**
 * Loads a service from a script file.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface ServiceLoader {

    /**
     * Returns a the variables that should be injected in the script. The
     * variables contain entries {@code [<variable name>=<value>, ...]}.
     * 
     * @return the variables {@link Map}.
     */
    Map<String, Object> getVariables();

    /**
     * Returns the registry to which to add the service.
     * 
     * @return the {@link ServicesRegistry}.
     */
    ServicesRegistry getRegistry();

    /**
     * Sets the optional dependencies for the services.
     * 
     * @param parent
     *            the parent {@link Object}.
     */
    void setParent(Object parent);

    /**
     * Sets the threads pool.
     * 
     * @param threads
     *            the {@link ExecutorService} threads pool.
     */
    void setThreads(ExecutorService threads);

    /**
     * Loads the service from the specified script file URL.
     * 
     * @param url
     *            the {@link URL} of the script file.
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
    ServicesRegistry loadService(URL url, ProfileService profile)
            throws ServiceException;

    /**
     * Loads the service from the specified script file URL.
     * 
     * @param url
     *            the {@link URL} of the script file.
     * 
     * @param profile
     *            the {@link ProfileService} or {@code null} if no profile is
     *            set.
     * 
     * @param prescript
     *            the {@link ServicePreScript} pre-script to be run.
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
    ServicesRegistry loadService(URL url, ProfileService profile,
            ServicePreScript prescript) throws ServiceException;
}
