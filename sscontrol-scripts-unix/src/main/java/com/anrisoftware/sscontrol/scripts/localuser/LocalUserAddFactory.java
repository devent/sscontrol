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
package com.anrisoftware.sscontrol.scripts.localuser;

import java.util.List;
import java.util.Map;

import com.anrisoftware.globalpom.exec.runcommands.RunCommands;
import com.anrisoftware.globalpom.threads.api.Threads;

/**
 * Factory to create to add local group.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface LocalUserAddFactory {

    /**
     * Create to add local group.
     *
     * @param args
     *            the {@link Map} arguments:
     *            <ul>
     *            <li>{@code runCommands} optionally, set to the
     *            {@link RunCommands} to record the command.
     *
     *            <li>{@code command} the group add command, for example
     *            {@code "/usr/sbin/useradd".}
     *
     *            <li>{@code usersFile} the path to the users file, for example
     *            {@code "/etc/passwd".}
     *
     *            <li>{@code userId} the user ID, for example {@code "100".}
     *
     *            <li>{@code userName} the user name, for example
     *            {@code "test".}
     *
     *            <li>{@code groupName} optionally, the user group name, for
     *            example {@code "test".}
     *
     *            <li>{@code systemUser} optionally, set to {@code true} to add
     *            a new system user.
     *
     *            <li>{@code groups} optionally, {@link List} of the local user
     *            groups, for example {@code "[foo, bar]".}
     *
     *            <li>{@code shell} optionally, set to the login shell.
     *
     *            <li>{@code homeDir} optionally, set to the home directories.
     *            </ul>
     *
     * @param parent
     *            the {@link Object} parent script.
     *
     * @param threads
     *            the {@link Threads} pool.
     *
     * @return the {@link LocalUserAdd}.
     */
    LocalUserAdd create(Map<String, Object> args, Object parent, Threads threads);
}
