/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-scripts-unix.
 *
 * sscontrol-scripts-unix is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-scripts-unix is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-scripts-unix. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.scripts.localuserinfo;

import java.util.Map;

import com.anrisoftware.globalpom.threads.api.Threads;

/**
 * Factory to create to local user information.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface LocalUserInfoFactory {

    /**
     * Create to local user information.
     *
     * @param args
     *            the {@link Map} arguments:
     *            <ul>
     *            <li>{@code command} the user information command, for example
     *            {@code "/usr/bin/id".}
     *
     *            <li>{@code userName} the user name, for example
     *            {@code "test".}
     *            </ul>
     *
     * @param parent
     *            the {@link Object} parent script.
     *
     * @param threads
     *            the {@link Threads} pool.
     *
     * @return the {@link LocalUserInfo}.
     */
    LocalUserInfo create(Map<String, Object> args, Object parent, Threads threads);
}
