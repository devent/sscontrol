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

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.capitalize;

import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.globalpom.exec.api.CommandLine;
import com.anrisoftware.globalpom.exec.api.ProcessTask;
import com.anrisoftware.globalpom.exec.script.ScriptCommandLineFactory;
import com.anrisoftware.globalpom.exec.scriptprocess.AbstractProcessExec;
import com.anrisoftware.globalpom.threads.api.Threads;
import com.anrisoftware.resources.templates.api.TemplateResource;
import com.google.inject.assistedinject.Assisted;

/**
 * Stops the specified services on the system.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class StopServices extends AbstractProcessExec {

    private final Map<String, Object> args;

    private final Object parent;

    @Inject
    private StopServicesLogger log;

    @Inject
    private CommandTemplatesProvider commandTemplates;

    private String system;

    /**
     * @see RestartServicesFactory#create(Map, Object, Threads)
     */
    @Inject
    StopServices(@Assisted Object parent, @Assisted Threads threads,
            @Assisted Map<String, Object> args) {
        super(threads, args);
        this.parent = parent;
        this.args = args;
        this.system = "unix";
    }

    public void setSystem(String system) {
        this.system = system;
    }

    @Override
    protected CommandLine createLine(ScriptCommandLineFactory commandLineFactory) {
        TemplateResource template = getTemplate();
        String name = format("%s%s", "stop", capitalize(system));
        return commandLineFactory.create(name, template).addSub("args", args);
    }

    @Override
    public ProcessTask call() throws Exception {
        log.checkArgs(args);
        ProcessTask task = super.call();
        log.stopDone(parent, task, args);
        return task;
    }

    private TemplateResource getTemplate() {
        return commandTemplates.get().getResource("stop");
    }

}
