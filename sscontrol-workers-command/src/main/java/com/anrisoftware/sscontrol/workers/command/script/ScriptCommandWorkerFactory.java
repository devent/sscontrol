/*
 * Copyright 2012-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-workers-command.
 *
 * sscontrol-workers-command is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-workers-command is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-workers-command. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.workers.command.script;

import java.util.Map;

import org.joda.time.Duration;

import com.anrisoftware.resources.templates.api.TemplateResource;
import com.anrisoftware.sscontrol.workers.api.WorkerFactory;
import com.anrisoftware.sscontrol.workers.command.exec.ExecCommandWorker;

/**
 * Factory to create a new worker that execute a shell script. The shell script
 * is created from a template.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface ScriptCommandWorkerFactory extends WorkerFactory {

    /**
     * Creates a new worker that executes a shell script. The shell script is
     * created from a template. The process will be terminated after
     * {@link ExecCommandWorker#TIMEOUT_DEFAULT}.
     * 
     * @param template
     *            the {@link TemplateResource} template resource.
     * 
     * @param attributes
     *            the template attributes.
     * 
     * @return the {@link ScriptCommandWorker}.
     */
    ScriptCommandWorker create(TemplateResource template, Object... attributes);

    /**
     * Creates a new worker that executes a shell script. The shell script is
     * created from a template and is executed with the specified environment
     * variables. The process will be terminated after
     * {@link ExecCommandWorker#TIMEOUT_DEFAULT}.
     * 
     * @param template
     *            the {@link TemplateResource} template resource.
     * 
     * @param environment
     *            a {@link Map} of the environment variables as
     *            {@code [<name>=<value>]}.
     * 
     * @param attributes
     *            the template attributes.
     * 
     * @return the {@link ScriptCommandWorker}.
     */
    ScriptCommandWorker create(TemplateResource template,
            Map<String, String> environment, Object... attributes);

    /**
     * Creates a new worker that executes a shell script. The shell script is
     * created from a template and is executed with the specified environment
     * variables. The process will be terminated after the specified timeout.
     * 
     * @param template
     *            the {@link TemplateResource} that returns the template.
     * 
     * @param environment
     *            a {@link Map} of the environment variables as
     *            {@code [<name>=<value>]}.
     * 
     * @param timeout
     *            the timeout {@link Duration}.
     * 
     * @param attributes
     *            the template attributes.
     * 
     * @return the {@link ScriptCommandWorker}.
     */
    ScriptCommandWorker create(TemplateResource template,
            Map<String, String> environment, Duration timeout,
            Object... attributes);
}
