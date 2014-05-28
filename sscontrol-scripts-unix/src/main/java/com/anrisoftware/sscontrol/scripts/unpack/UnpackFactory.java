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
package com.anrisoftware.sscontrol.scripts.unpack;

import java.io.File;
import java.util.Map;

import com.anrisoftware.globalpom.threads.api.Threads;

/**
 * Factory to create unpack archives.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface UnpackFactory {

    /**
     * Creates to unpack archives.
     * 
     * @param args
     *            the {@link Map} arguments:
     *            <ul>
     *            <li>{@code file} the {@link File} to unpack;
     * 
     *            <li>{@code output} the output {@link File} directory;
     * 
     *            <li>{@code override} optionally, set to {@code true} to
     *            override existing files;
     * 
     *            <li>{@code strip} optionally, set to {@code true} to strip the
     *            first level directory from the archive;
     * 
     *            <li>{@code commands} set the {@link Map} of command to unpack
     *            the archive for each archive type.
     *            <ul>
     *            <li>{@code tgz} tar/gz archive for extension {@code .tar.gz}
     *            <li>{@code zip} Zip archive for extension {@code .zip}
     *            </ul>
     *            </ul>
     * 
     * @param parent
     *            the {@link Object} parent script.
     * 
     * @param threads
     *            the {@link Threads} pool.
     * 
     * @return the {@link Unpack}.
     */
    Unpack create(Map<String, Object> args, Object parent, Threads threads);
}
