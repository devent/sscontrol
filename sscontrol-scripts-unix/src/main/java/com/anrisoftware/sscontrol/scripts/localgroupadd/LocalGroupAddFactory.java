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
package com.anrisoftware.sscontrol.scripts.localgroupadd;

import java.util.Map;

import com.anrisoftware.globalpom.threads.api.Threads;

/**
 * Factory to create the check if the repository was enabled.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface LocalGroupAddFactory {

    /**
     * Create the check if the repository was enabled.
     * 
     * @param args
     *            the {@link Map} arguments:
     *            <ul>
     *            <li>{@code command} the group add command, for example
     *            {@code "/usr/sbin/groupadd".}
     *            <li>{@code systemGroup} optionally, set to {@code true} to add
     *            a new system group.
     *            <li>{@code groupsFile} the path to the groups file, for
     *            example {@code "/etc/group".}
     *            <li>{@code groupId} the group ID, for example {@code "100".}
     *            <li>{@code groupName} the group name, for example
     *            {@code "test".}
     *            </ul>
     * 
     * @param parent
     *            the {@link Object} parent script.
     * 
     * @param threads
     *            the {@link Threads} pool.
     * 
     * @return the {@link LocalGroupAdd}.
     */
    LocalGroupAdd create(Map<String, Object> args, Object parent,
            Threads threads);
}
