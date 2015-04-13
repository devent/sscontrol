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
package com.anrisoftware.sscontrol.scripts.findusedport;

import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.globalpom.exec.api.CommandLine;
import com.anrisoftware.globalpom.exec.api.ProcessTask;
import com.anrisoftware.globalpom.exec.script.ScriptCommandExec;
import com.anrisoftware.globalpom.exec.script.ScriptCommandLineFactory;
import com.anrisoftware.globalpom.exec.scriptprocess.AbstractProcessExec;
import com.anrisoftware.globalpom.threads.api.Threads;
import com.anrisoftware.resources.templates.api.TemplateResource;
import com.anrisoftware.resources.templates.api.TemplatesFactory;
import com.google.inject.assistedinject.Assisted;

/**
 * Starts the <i>netstat</i> process to list the used ports and services.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class FindUsedPortProcess extends AbstractProcessExec {

    private static final String TEMPLATE_NAME = "listports";

    private static final String TEMPLATES_NAME = "ScriptsUnixTemplates";

    private static final String ARGS = "args";

    private final Map<String, Object> args;

    @Inject
    private TemplatesFactory templatesFactory;

    /**
     * @see FindUsedPortProcessFactory#create(Threads, Map)
     */
    @Inject
    FindUsedPortProcess(@Assisted Threads threads,
            @Assisted Map<String, Object> args) {
        super(threads, args);
        this.args = args;
    }

    @Override
    public ProcessTask call() throws Exception {
        ProcessTask task = super.call();
        return task;
    }

    @Override
    protected CommandLine createLine(ScriptCommandLineFactory commandLineFactory) {
        String name = "netstat";
        TemplateResource template = templatesFactory.create(TEMPLATES_NAME)
                .getResource(TEMPLATE_NAME);
        return commandLineFactory.create(name, template).addSub(ARGS, args);
    }

    @Override
    protected void setupCommandOutput(ScriptCommandExec script, CommandLine line) {
        script.setCommandOutput(null);
    }
}
