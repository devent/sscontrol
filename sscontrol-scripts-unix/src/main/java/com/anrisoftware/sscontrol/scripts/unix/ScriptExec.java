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

import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.globalpom.exec.api.CommandLine;
import com.anrisoftware.globalpom.exec.api.ProcessTask;
import com.anrisoftware.globalpom.exec.script.ScriptCommandLineFactory;
import com.anrisoftware.globalpom.threads.api.Threads;
import com.anrisoftware.resources.templates.api.TemplateResource;
import com.google.inject.assistedinject.Assisted;

/**
 * Executes the script from a template.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class ScriptExec extends AbstractProcessExec {

    private static final String ARGS = "args";

    private final Map<String, Object> args;

    private final Object parent;

    private final String name;

    private final TemplateResource templateResource;

    @Inject
    private ScriptExecLogger log;

    /**
     * @see ScriptExecFactory#create(Object, Threads, TemplateResource, String,
     *      Map)
     */
    @Inject
    ScriptExec(@Assisted Object parent, @Assisted Threads threads,
            @Assisted TemplateResource templateResource, @Assisted String name,
            @Assisted Map<String, Object> args) {
        super(threads, args);
        this.args = args;
        this.parent = parent;
        this.name = name;
        this.templateResource = templateResource;
    }

    @Override
    public ProcessTask call() throws Exception {
        ProcessTask task = super.call();
        log.scriptDone(parent, task, args);
        return task;
    }

    @Override
    protected CommandLine createLine(ScriptCommandLineFactory commandLineFactory) {
        return commandLineFactory.create(name, templateResource).addSub(ARGS,
                args);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(args).toString();
    }
}
