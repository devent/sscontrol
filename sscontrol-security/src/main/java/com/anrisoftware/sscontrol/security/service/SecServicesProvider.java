/*
 * Copyright 2013-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-security.
 *
 * sscontrol-security is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-security is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-security. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.security.service;

import java.util.ServiceLoader;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import com.anrisoftware.sscontrol.core.api.ServiceException;

/**
 * Loads the security services.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
public class SecServicesProvider implements
        Provider<ServiceLoader<SecServiceFactoryFactory>> {

    private final ServiceLoader<SecServiceFactoryFactory> services;

    @Inject
    private SecServicesProviderLogger log;

    public SecServicesProvider() {
        this.services = ServiceLoader.load(SecServiceFactoryFactory.class);
    }

    @Override
    public ServiceLoader<SecServiceFactoryFactory> get() {
        return services;
    }

    /**
     * Find the security service for the specified info.
     *
     * @param info
     *            the {@link SecServiceInfo}.
     *
     * @return the {@link SecServiceFactoryFactory}
     *
     * @throws ServiceException
     *             if the service could not be found.
     */
    public SecServiceFactoryFactory find(SecServiceInfo info)
            throws ServiceException {
        for (SecServiceFactoryFactory factory : get()) {
            if (factory.getInfo().equals(info)) {
                return factory;
            }
        }
        throw log.errorFindService(info);
    }
}
