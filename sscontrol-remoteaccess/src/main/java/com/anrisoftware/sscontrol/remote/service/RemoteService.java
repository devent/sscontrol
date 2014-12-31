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
package com.anrisoftware.sscontrol.remote.service;

import java.util.List;
import java.util.Map;

import com.anrisoftware.sscontrol.core.api.Service;
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
     * Returns the debug logging for the specified key.
     * <p>
     * The example returns the following map for the key "level":
     *
     * <pre>
     * {["sshd": 5]}
     * </pre>
     *
     * <pre>
     * remote {
     *     debug "sshd", level: 4
     * }
     * </pre>
     *
     * @return the {@link Map} of the debug levels or {@code null}.
     */
    Map<String, Object> debugLogging(String key);

    /**
     * Returns the binding address.
     *
     * <pre>
     * remote {
     *      bind "192.168.0.1"
     * }
     * </pre>
     *
     * @return the {@link String} address or {@code null}.
     */
    String getBinding();

    /**
     * Returns the binding port.
     *
     * <pre>
     * remote {
     *      bind "192.168.0.1", port: 22
     * }
     * </pre>
     *
     * @return the {@link Integer} port or {@code null}.
     */
    Integer getBindingPort();

    /**
     * Returns the local users.
     *
     * <pre>
     * remote {
     *     user "foo", password: "foopass", {
     *     }
     * }
     * </pre>
     *
     * @return the {@link List} of local {@link User} users.
     */
    List<User> getUsers();

}
