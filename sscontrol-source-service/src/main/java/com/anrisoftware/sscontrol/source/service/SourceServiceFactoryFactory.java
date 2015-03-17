/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-source-service.
 *
 * sscontrol-source-service is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-source-service is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-source-service. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.source.service;

import com.anrisoftware.sscontrol.core.api.ServiceException;

/**
 * Source code management service factory.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface SourceServiceFactoryFactory {

    /**
     * Returns the source code management service factory.
     *
     * @return the service {@link SourceServiceFactory} factory.
     *
     * @throws ServiceException
     *             if there was an error returning the service factory.
     */
    SourceServiceFactory getFactory() throws ServiceException;

    /**
     * Returns the information by which the source code management service is
     * identified.
     *
     * @return the {@link SourceServiceInfo}.
     */
    SourceServiceInfo getInfo();

    /**
     * Sets the parent for the service.
     *
     * @param parent
     *            the parent.
     */
    void setParent(Object parent);

    /**
     * Configures the compiler for the service.
     *
     * @param compiler
     *            the compiler.
     *
     * @throws Exception
     *             if some error occur.
     */
    void configureCompiler(Object compiler) throws Exception;
}
