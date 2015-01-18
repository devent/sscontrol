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
package com.anrisoftware.sscontrol.scripts.killprocess;

import static com.anrisoftware.sscontrol.scripts.killprocess.KillProcessLogger.TYPE;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.regex.Pattern;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.globalpom.exec.api.ProcessTask;
import com.anrisoftware.globalpom.exec.scriptprocess.ScriptExecFactory;
import com.anrisoftware.globalpom.threads.api.Threads;
import com.anrisoftware.resources.templates.api.AttributeRenderer;
import com.anrisoftware.resources.templates.api.TemplateResource;
import com.anrisoftware.resources.templates.api.Templates;
import com.anrisoftware.resources.templates.api.TemplatesFactory;
import com.google.inject.assistedinject.Assisted;

/**
 * Kills a process.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class KillProcess implements Callable<KillProcess> {

    private static final String KILL_TEMPLATE = "kill";

    private static final String SCRIPTS_UNIX_TEMPLATES = "ScriptsUnixTemplates";

    private static final Pattern NO_SUCH_PROCESS_PATTERN = Pattern
            .compile("kill: kill (\\d+) failed: no such process");

    private final Map<String, Object> args;

    private final Threads threads;

    private final Object parent;

    @Inject
    private KillProcessLogger log;

    @Inject
    private ScriptExecFactory scriptExecFactory;

    @Inject
    private KillTypeRenderer killTypeRenderer;

    private TemplateResource templateResource;

    private boolean processKilled;

    /**
     * @see KillProcessFactory#create(Map, Object, Threads)
     */
    @Inject
    KillProcess(@Assisted Map<String, Object> args, @Assisted Object parent,
            @Assisted Threads threads) {
        this.args = setupDefaults(new HashMap<String, Object>(args));
        this.parent = parent;
        this.threads = threads;
    }

    private Map<String, Object> setupDefaults(Map<String, Object> args) {
        if (!args.containsKey(TYPE)) {
            args.put(TYPE, KillType.TERM);
        }
        return args;
    }

    @Inject
    public void setTemplatesFactory(TemplatesFactory templatesFactory) {
        Map<Serializable, Serializable> attributes = new HashMap<Serializable, Serializable>();
        ArrayList<AttributeRenderer> renderers = new ArrayList<AttributeRenderer>();
        renderers.add(killTypeRenderer);
        attributes.put("renderers", renderers);
        Templates templates;
        templates = templatesFactory.create(SCRIPTS_UNIX_TEMPLATES, attributes);
        this.templateResource = templates.getResource(KILL_TEMPLATE);
    }

    @Override
    public KillProcess call() throws Exception {
        log.checkCommand(args, parent);
        log.checkProcess(args, parent);
        args.put("checkExitCodes", false);
        args.put("exitCodes", new int[] { 0, 1 });
        args.put("errString", true);
        ProcessTask process = scriptExecFactory.create(args, parent, threads,
                templateResource, "unix").call();
        if (process.getExitValue() == 0) {
            this.processKilled = true;
        } else if (NO_SUCH_PROCESS_PATTERN.matcher(process.getErr()).matches()) {
            this.processKilled = false;
        }
        return this;
    }

    public boolean isProcessKilled() {
        return processKilled;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).toString();
    }

}
