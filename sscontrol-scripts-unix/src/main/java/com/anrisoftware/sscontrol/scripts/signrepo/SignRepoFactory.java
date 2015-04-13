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
package com.anrisoftware.sscontrol.scripts.signrepo;

import java.io.File;
import java.net.URL;
import java.util.Map;

import com.anrisoftware.globalpom.threads.api.Threads;

/**
 * Factory to create the repository signer.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface SignRepoFactory {

    /**
     * Creates the repository signer.
     *
     * @param args
     *            the {@link Map} arguments:
     *            <ul>
     *            <li>{@code command} the sign repository command, for example
     *            {@code "/usr/bin/apt-key".}
     *
     *            <li>{@code key} the repository key {@link URL} resource.
     *
     *            <li>{@code system} the system name, for example
     *            {@code "ubuntu"}.
     *
     *            <li>{@code tmp} the temporary {@link File} directory.
     *            </ul>
     *
     * @param parent
     *            the {@link Object} parent script.
     *
     * @param threads
     *            the {@link Threads} pool.
     *
     * @return the {@link SignRepo}.
     */
    SignRepo create(Map<String, Object> args, Object parent, Threads threads);
}
