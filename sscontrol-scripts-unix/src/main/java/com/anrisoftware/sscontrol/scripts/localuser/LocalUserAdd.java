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
package com.anrisoftware.sscontrol.scripts.localuser;

import java.util.Map;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.globalpom.exec.api.ProcessTask;
import com.anrisoftware.globalpom.exec.scriptprocess.ScriptExecFactory;
import com.anrisoftware.globalpom.threads.api.Threads;
import com.anrisoftware.resources.templates.api.TemplateResource;
import com.anrisoftware.resources.templates.api.Templates;
import com.anrisoftware.resources.templates.api.TemplatesFactory;
import com.google.inject.assistedinject.Assisted;

/**
 * Adds the local user.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class LocalUserAdd implements Callable<LocalUserAdd> {

    private static final String UNIX = "unix";

    private static final String TEMPLATE_NAME = "useradd";

    private static final String TEMPLATES_NAME = "ScriptsUnixTemplates";

    private final LocalUserAddLogger log;

    private final Map<String, Object> args;

    private final Threads threads;

    private final Object parent;

    @Inject
    private ScriptExecFactory scriptExecFactory;

    @Inject
    private TemplatesFactory templatesFactory;

    private boolean userAdded;

    /**
     * @see LocalUserAddFactory#create(Map, Object, Threads)
     */
    @Inject
    LocalUserAdd(LocalUserAddLogger log, @Assisted Map<String, Object> args,
            @Assisted Object parent, @Assisted Threads threads) {
        this.log = log;
        this.args = args;
        this.parent = parent;
        this.threads = threads;
        args.put("outString", true);
        log.userName(args, parent);
        log.groupName(args, parent);
        log.userId(args, parent);
        log.usersFile(args, parent);
        log.systemUser(args, parent);
        log.homeDir(args, parent);
        log.shell(args, parent);
        log.command(args, parent);
    }

    @Override
    public LocalUserAdd call() throws Exception {
        Templates templates = templatesFactory.create(TEMPLATES_NAME);
        TemplateResource templateResource = templates
                .getResource(TEMPLATE_NAME);
        ProcessTask task = scriptExecFactory.create(args, parent, threads,
                templateResource, UNIX).call();
        checkUserAdded(task);
        return this;
    }

    private void checkUserAdded(ProcessTask task) {
        String out = task.getOut().trim();
        if (out.endsWith("added.")) {
            userAdded = true;
            log.userAdded(parent, task, args);
        } else if (out.endsWith("already exists.")) {
            userAdded = false;
            log.userAlreadyExist(parent, task, args);
        }
    }

    public boolean isUserAdded() {
        return userAdded;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).toString();
    }

}
