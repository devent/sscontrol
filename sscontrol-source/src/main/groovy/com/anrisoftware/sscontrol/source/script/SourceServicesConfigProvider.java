/*
 * Copyright 2015-2015 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.source.script;

import java.util.ServiceLoader;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.security.service.ServiceConfigFactory;
import com.anrisoftware.sscontrol.security.service.SourceServiceConfigInfo;

/**
 * Loads the source code management service configurations.
 *
 * @see ServiceConfigFactory
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
public class SourceServicesConfigProvider implements
        Provider<ServiceLoader<ServiceConfigFactory>> {

    private final ServiceLoader<ServiceConfigFactory> services;

    @Inject
    private SourceServicesConfigProviderLogger log;

    public SourceServicesConfigProvider() {
        this.services = ServiceLoader.load(ServiceConfigFactory.class);
    }

    @Override
    public ServiceLoader<ServiceConfigFactory> get() {
        return services;
    }

    /**
     * Find the security service configuration for the specified info.
     *
     * @param info
     *            the {@link SourceServiceConfigInfo}.
     *
     * @return the {@link ServiceConfigFactory}
     *
     * @throws ServiceException
     *             if the service could not be found.
     */
    public ServiceConfigFactory find(SourceServiceConfigInfo info)
            throws ServiceException {
        for (ServiceConfigFactory factory : get()) {
            if (factory.getInfo().equals(info)) {
                return factory;
            }
        }
        throw log.errorFindService(info);
    }
}
