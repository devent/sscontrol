/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-source.
 *
 * sscontrol-source is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-source is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-source. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.security.service;

import java.util.ServiceLoader;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.source.service.SourceServiceFactoryFactory;
import com.anrisoftware.sscontrol.source.service.SourceServiceInfo;

/**
 * Loads the security services.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
public class SourceServicesProvider implements
        Provider<ServiceLoader<SourceServiceFactoryFactory>> {

    private final ServiceLoader<SourceServiceFactoryFactory> services;

    @Inject
    private SourceServicesProviderLogger log;

    public SourceServicesProvider() {
        this.services = ServiceLoader.load(SourceServiceFactoryFactory.class);
    }

    @Override
    public ServiceLoader<SourceServiceFactoryFactory> get() {
        return services;
    }

    /**
     * Find the security service for the specified info.
     *
     * @param info
     *            the {@link SourceServiceInfo}.
     *
     * @return the {@link SourceServiceFactoryFactory}
     *
     * @throws ServiceException
     *             if the service could not be found.
     */
    public SourceServiceFactoryFactory find(SourceServiceInfo info)
            throws ServiceException {
        for (SourceServiceFactoryFactory factory : get()) {
            if (factory.getInfo().equals(info)) {
                return factory;
            }
        }
        throw log.errorFindService(info);
    }
}
