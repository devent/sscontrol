/*
 * Copyright 2012-2015 Erwin Müller <erwin.mueller@deventm.org>
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

/**
 * Factory that creates the pre service script for the specified service
 * information.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface ServicePreScriptFactory {

    /**
     * Returns the service pre-script.
     * 
     * @return the service {@link ServicePreScript}.
     * 
     * @throws ServiceException
     *             if there was an error returning the service script.
     */
    ServicePreScript getPreScript() throws ServiceException;

    /**
     * Returns the information by which the service pre-script is identified.
     * 
     * @return the {@link ServiceScriptInfo}.
     */
    ServiceScriptInfo getInfo();

    /**
     * Sets the parent for the service pre-script.
     * 
     * @param parent
     *            the parent.
     */
    void setParent(Object parent);

}
