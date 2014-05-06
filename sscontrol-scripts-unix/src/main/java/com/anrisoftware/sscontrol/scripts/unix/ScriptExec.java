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
import java.util.concurrent.Callable;

import javax.inject.Inject;

import org.slf4j.Logger;

import com.anrisoftware.globalpom.exec.api.CommandLine;
import com.anrisoftware.globalpom.exec.api.ProcessTask;
import com.anrisoftware.globalpom.exec.core.DefaultCommandExecFactory;
import com.anrisoftware.globalpom.exec.logoutputs.DebugLogCommandOutputFactory;
import com.anrisoftware.globalpom.exec.logoutputs.ErrorLogCommandOutputFactory;
import com.anrisoftware.globalpom.exec.script.ScriptCommandExec;
import com.anrisoftware.globalpom.exec.script.ScriptCommandExecFactory;
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
public class ScriptExec implements Callable<ProcessTask> {

    private final Map<String, Object> args;

    private final Object parent;

    private final Threads threads;

    private final String name;

    private final TemplateResource templateResource;

    @Inject
    private ScriptExecLogger log;

    @Inject
    private ScriptCommandExecFactory scriptExecFactory;

    @Inject
    private ScriptCommandLineFactory lineFactory;

    @Inject
    private DefaultCommandExecFactory execFactory;

    @Inject
    private DebugLogCommandOutputFactory debugOutputFactory;

    @Inject
    private ErrorLogCommandOutputFactory errorOutputFactory;

    /**
     * @see ScriptExecFactory#create(Object, Threads, TemplateResource, String,
     *      Map)
     */
    @Inject
    ScriptExec(@Assisted Object parent, @Assisted Threads threads,
            @Assisted TemplateResource templateResource, @Assisted String name,
            @Assisted Map<String, Object> args) {
        this.parent = parent;
        this.args = args;
        this.threads = threads;
        this.name = name;
        this.templateResource = templateResource;
    }

    @Override
    public ProcessTask call() throws Exception {
        log.checkArgs(this, args);
        CommandLine line = createLine(templateResource);
        ScriptCommandExec script = createExec();
        ProcessTask task = exec(line, script);
        log.scriptDone(parent, task, args);
        return task;
    }

    private ScriptCommandExec createExec() {
        ScriptCommandExec script = scriptExecFactory.create(execFactory);
        script.setThreads(threads);
        return script;
    }

    private ProcessTask exec(CommandLine line, ScriptCommandExec script)
            throws Exception {
        Logger logger = (Logger) args.get("log");
        script.setCommandError(errorOutputFactory.create(logger, line));
        script.setCommandOutput(debugOutputFactory.create(logger, line));
        ProcessTask task = script.exec(line).get();
        return task;
    }

    private CommandLine createLine(TemplateResource template) {
        return lineFactory.create(name, template).addSub("args", args);
    }

}
