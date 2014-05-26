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
package com.anrisoftware.sscontrol.security.services;

import java.util.Map;

/**
 * Factory to create service to secure.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface ServiceFactory {

    /**
     * Creates service to secure.
     * 
     * @param service
     *            the remote access
     *            {@link com.anrisoftware.sscontrol.core.api.Service}.
     * 
     * @param args
     *            the {@link Map} arguments.
     * 
     * @return the {@link Service}.
     */
    Service create(com.anrisoftware.sscontrol.core.api.Service service,
            Map<String, Object> args);
}
