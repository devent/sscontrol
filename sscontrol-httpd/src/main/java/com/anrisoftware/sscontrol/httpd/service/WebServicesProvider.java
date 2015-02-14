/*
 * Copyright 2012-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd.
 *
 * sscontrol-httpd is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.service;

import java.util.ServiceLoader;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.httpd.webservice.WebServiceFactoryFactory;
import com.anrisoftware.sscontrol.httpd.webservice.WebServiceInfo;

/**
 * Loads the web services.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
public class WebServicesProvider implements
        Provider<ServiceLoader<WebServiceFactoryFactory>> {

    private final ServiceLoader<WebServiceFactoryFactory> services;

    @Inject
    private WebServicesProviderLogger log;

    public WebServicesProvider() {
        this.services = ServiceLoader.load(WebServiceFactoryFactory.class);
    }

    @Override
    public ServiceLoader<WebServiceFactoryFactory> get() {
        return services;
    }

    /**
     * Find the web service for the specified info.
     * 
     * @param info
     *            the {@link WebServiceInfo}.
     * 
     * @return the {@link WebServiceFactoryFactory}
     * 
     * @throws ServiceException
     *             if the service could not be found.
     */
    public WebServiceFactoryFactory find(WebServiceInfo info)
            throws ServiceException {
        for (WebServiceFactoryFactory factory : get()) {
            if (factory.getInfo().equals(info)) {
                return factory;
            }
        }
        throw log.errorFindService(info);
    }
}
