/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-nginx.
 *
 * sscontrol-httpd-nginx is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-nginx is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-nginx. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.nginx.nginx.linux;

import java.util.ServiceLoader;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.httpd.webservice.ServiceConfigFactory;
import com.anrisoftware.sscontrol.httpd.webservice.ServiceConfigInfo;
import com.anrisoftware.sscontrol.httpd.webservice.WebServiceFactoryFactory;

/**
 * Loads the web service configurations.
 * 
 * @see ServiceConfigFactory
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
public class WebServicesConfigProvider implements
        Provider<ServiceLoader<ServiceConfigFactory>> {

    private final ServiceLoader<ServiceConfigFactory> services;

    @Inject
    private WebServicesConfigProviderLogger log;

    public WebServicesConfigProvider() {
        this.services = ServiceLoader.load(ServiceConfigFactory.class);
    }

    @Override
    public ServiceLoader<ServiceConfigFactory> get() {
        return services;
    }

    /**
     * Find the web service configuration for the specified info.
     * 
     * @param info
     *            the {@link ServiceConfigInfo}.
     * 
     * @return the {@link WebServiceFactoryFactory}
     * 
     * @throws ServiceException
     *             if the service could not be found.
     */
    public ServiceConfigFactory find(ServiceConfigInfo info)
            throws ServiceException {
        for (ServiceConfigFactory factory : get()) {
            if (factory.getInfo().equals(info)) {
                return factory;
            }
        }
        throw log.errorFindService(info);
    }
}
