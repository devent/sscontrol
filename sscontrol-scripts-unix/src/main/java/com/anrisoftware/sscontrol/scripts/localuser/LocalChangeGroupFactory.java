/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.scripts.localuser;

import java.util.Map;

import com.anrisoftware.globalpom.exec.runcommands.RunCommands;
import com.anrisoftware.globalpom.threads.api.Threads;

/**
 * Factory to create to modify the group user.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface LocalChangeGroupFactory {

    /**
     * Create to modify the local group.
     *
     * @param args
     *            the {@link Map} arguments:
     *            <ul>
     *            <li>{@code runCommands} optionally, set to the
     *            {@link RunCommands} to record the command.
     *
     *            <li>{@code command} the change file owner command, for example
     *            {@code "/usr/sbin/groupmod".}
     *
     *            <li>{@code groupName} the local group name.
     *
     *            <li>{@code groupId} optionally, the new group ID, for example
     *            {@code 100}.
     *            </ul>
     *
     * @param parent
     *            the {@link Object} parent script.
     *
     * @param threads
     *            the {@link Threads} pool.
     *
     * @return the {@link LocalChangeGroup}.
     */
    LocalChangeGroup create(Map<String, Object> args, Object parent,
            Threads threads);
}
