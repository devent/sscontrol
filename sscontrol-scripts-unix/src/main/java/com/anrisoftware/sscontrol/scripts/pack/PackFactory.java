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
package com.anrisoftware.sscontrol.scripts.pack;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.joda.time.Duration;

import com.anrisoftware.globalpom.exec.api.ProcessTask;
import com.anrisoftware.globalpom.exec.runcommands.RunCommands;
import com.anrisoftware.globalpom.exec.scriptprocess.AbstractProcessExec;
import com.anrisoftware.globalpom.threads.api.Threads;
import com.anrisoftware.sscontrol.scripts.unix.InstallPackages;

/**
 * Factory to create the archive from specified files.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface PackFactory {

    /**
     * Creates archive from specified files.
     *
     * @param args
     *            the {@link Map} arguments:
     *            <ul>
     *            <li>{@code runCommands} optionally, set to the
     *            {@link RunCommands} to record the command.
     *
     *            <li>{@code files} the {@link File} files to pack;
     *
     *            <li>{@code output} the output {@link File} archive;
     *
     *            <li>{@code commands} set the {@link Map} of command to unpack
     *            the archive for each archive type.
     *            <ul>
     *            <li>{@code tgz} tar/gz archive for extension {@code .tar.gz}
     *            <li>{@code zip} Zip archive for extension {@code .zip}
     *            </ul>
     *
     *            <li>{@code outString} optionally, set to {@code true} to save
     *            the output in a {@link String} for later parsing, see
     *            {@link ProcessTask#getOut()}. Per default it is set to
     *            {@link AbstractProcessExec#OUT_STRING_DEFAULT}.
     *
     *            <li>{@code timeout} optionally, set the timeout
     *            {@link Duration}. Per default it is set to
     *            {@link InstallPackages#TIMEOUT_DEFAULT}.
     *
     *            <li>{@code destroyOnTimeout} optionally, set to {@code true}
     *            to destroy the process on timeout. Per default it is set to
     *            {@link AbstractProcessExec#DESTROY_ON_TIMEOUT_DEFAULT}.
     *
     *            <li>{@code checkExitCodes} optionally, set to {@code true} to
     *            check the exit code(s) of the process;
     *
     *            <li>{@code exitCodes} optionally, set a {@link List} of
     *            success exit codes;
     *
     *            <li>{@code exitCode} optionally, set the success exit code of
     *            the process. Per default it is set to
     *            {@link AbstractProcessExec#EXIT_CODE_DEFAULT}.
     *            </ul>
     *
     * @param parent
     *            the {@link Object} parent script.
     *
     * @param threads
     *            the {@link Threads} pool.
     *
     * @return the {@link Pack}.
     */
    Pack create(Map<String, Object> args, Object parent, Threads threads);
}
