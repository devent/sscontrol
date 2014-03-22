/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-webservice.
 *
 * sscontrol-httpd-webservice is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-webservice is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-webservice. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.webservice;

import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.core.api.ServiceScriptInfo;

/**
 * Service configuration factory.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface ServiceConfigFactory {

    /**
     * Returns the service configuration.
     * 
     * @return the service {@link ServiceConfig} configuration.
     * 
     * @throws ServiceException
     *             if there was an error returning the service configuration.
     */
    ServiceConfig getScript() throws ServiceException;

    /**
     * Returns the information by which the service configuration is identified.
     * 
     * @return the {@link ServiceScriptInfo}.
     */
    ServiceConfigInfo getInfo();

    /**
     * Sets the parent for the service configuration.
     * 
     * @param parent
     *            the parent.
     */
    void setParent(Object parent);

}
