/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.remote.service;

import java.util.List;

import com.anrisoftware.sscontrol.core.api.Service;
import com.anrisoftware.sscontrol.core.bindings.Binding;
import com.anrisoftware.sscontrol.core.debuglogging.DebugLogging;
import com.anrisoftware.sscontrol.remote.user.User;

/**
 * Remote access service.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface RemoteService extends Service {

    /**
     * Returns the remote access service name.
     */
    @Override
    String getName();

    /**
     * Returns the local users.
     * 
     * @return the {@link List} of local {@link User} users.
     */
    List<User> getUsers();

    /**
     * Returns a list of the IP addresses where to bind the remote service.
     * 
     * @return the {@link Binding}.
     */
    Binding getBinding();

    /**
     * Sets the debug logging.
     * 
     * @param debug
     *            the {@link DebugLogging}.
     */
    void setDebug(DebugLogging debug);

    /**
     * Returns debug logging.
     * 
     * @return the {@link DebugLogging} or {@code null}.
     */
    DebugLogging getDebug();

}
