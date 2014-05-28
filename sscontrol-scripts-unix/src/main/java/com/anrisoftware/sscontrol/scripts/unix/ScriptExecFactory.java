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
package com.anrisoftware.sscontrol.scripts.unix;

import java.util.List;
import java.util.Map;

import org.joda.time.Duration;

import com.anrisoftware.globalpom.exec.api.ProcessTask;
import com.anrisoftware.globalpom.threads.api.Threads;
import com.anrisoftware.resources.templates.api.TemplateResource;

/**
 * Factory to create the script exec.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface ScriptExecFactory {

    /**
     * Create the script exec.
     * 
     * @param args
     *            the {@link Map} arguments:
     * 
     *            <ul>
     *            <li>{@code log} the logger that logs the command output;
     * 
     *            <li>{@code outString} optionally, set to {@code true} to save
     *            the output in a {@link String} for later parsing, see
     *            {@link ProcessTask#getOut()};
     * 
     *            <li>{@code timeout} optionally, set the timeout
     *            {@link Duration};
     * 
     *            <li>{@code destroyOnTimeout} optionally, set to {@code true}
     *            to destroy the process on timeout;
     * 
     *            <li>{@code exitCodes} optionally, set a {@link List} of
     *            success exit codes;
     * 
     *            <li>{@code exitCode} optionally, set the success exit code of
     *            the process;
     *            </ul>
     * 
     * @param parent
     *            the {@link Object} parent script.
     * 
     * @param threads
     *            the {@link Threads} pool.
     * 
     * @param templateResource
     *            the {@link TemplateResource}.
     * 
     * @param name
     *            the {@link String} name.
     * 
     * @return the {@link ScriptExec}.
     */
    ScriptExec create(Map<String, Object> args, Object parent, Threads threads,
            TemplateResource templateResource, String name);
}
