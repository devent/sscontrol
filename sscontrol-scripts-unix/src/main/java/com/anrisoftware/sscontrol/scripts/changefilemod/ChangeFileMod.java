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
package com.anrisoftware.sscontrol.scripts.changefilemod;

import java.util.Map;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.globalpom.exec.api.ProcessTask;
import com.anrisoftware.globalpom.threads.api.Threads;
import com.anrisoftware.resources.templates.api.TemplateResource;
import com.anrisoftware.resources.templates.api.Templates;
import com.anrisoftware.resources.templates.api.TemplatesFactory;
import com.anrisoftware.sscontrol.scripts.unix.ScriptExecFactory;
import com.google.inject.assistedinject.Assisted;

/**
 * Change the mode of the file.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class ChangeFileMod implements Callable<ChangeFileMod> {

    private final ChangeFileModLogger log;

    private final Map<String, Object> args;

    private final Threads threads;

    private final Object parent;

    @Inject
    private ScriptExecFactory scriptExecFactory;

    @Inject
    private TemplatesFactory templatesFactory;

    /**
     * @see ChangeFileModFactory#create(Map, Object, Threads)
     */
    @Inject
    ChangeFileMod(ChangeFileModLogger log, @Assisted Map<String, Object> args,
            @Assisted Object parent, @Assisted Threads threads) {
        this.log = log;
        this.args = args;
        this.parent = parent;
        this.threads = threads;
        log.command(args, parent);
        log.mod(args, parent);
        log.files(args, parent);
        log.recursive(args, parent);
    }

    @Override
    public ChangeFileMod call() throws Exception {
        Templates templates = templatesFactory.create("ScriptsUnixTemplates");
        TemplateResource templateResource = templates.getResource("chmod");
        ProcessTask task = scriptExecFactory.create(args, parent, threads,
                templateResource, "unix").call();
        log.modChanged(parent, task, args);
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).toString();
    }

}