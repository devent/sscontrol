/*
 * Copyright 2013-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-remoteaccess.
 *
 * sscontrol-remoteaccess is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-remoteaccess is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-remoteaccess. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.remote.user;

import java.util.Map;

import com.anrisoftware.sscontrol.core.api.Service;

/**
 * Factory to create local user group.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface GroupFactory {

    /**
     * Creates local user group.
     * 
     * @param service
     *            the remote access {@link Service}.
     * 
     * @param name
     *            the group {@link String} name.
     * 
     * @return the {@link Group}.
     */
    Group create(Service service, String name);

    /**
     * Creates local user group.
     * 
     * @param service
     *            the remote access {@link Service}.
     * 
     * @param args
     *            the {@link Map} arguments.
     * 
     * @return the {@link Group}.
     */
    Group create(Service service, Map<String, Object> args);

}
