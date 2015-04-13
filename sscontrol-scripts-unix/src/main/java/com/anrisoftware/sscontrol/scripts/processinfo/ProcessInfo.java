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
package com.anrisoftware.sscontrol.scripts.processinfo;

import static com.anrisoftware.sscontrol.scripts.processinfo.ProcessInfoLogger.SEARCH;
import static java.util.Collections.unmodifiableSet;
import static org.apache.commons.lang3.StringUtils.join;
import static org.apache.commons.lang3.StringUtils.split;

import java.text.ParseException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.globalpom.exec.api.ProcessTask;
import com.anrisoftware.globalpom.exec.scriptprocess.ScriptExecFactory;
import com.anrisoftware.globalpom.threads.api.Threads;
import com.anrisoftware.resources.templates.api.TemplateResource;
import com.anrisoftware.resources.templates.api.Templates;
import com.anrisoftware.resources.templates.api.TemplatesFactory;
import com.anrisoftware.sscontrol.scripts.scriptsexceptions.ScriptException;
import com.google.inject.assistedinject.Assisted;

/**
 * Search a specific process and returns information about that process.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class ProcessInfo implements Callable<ProcessInfo> {

    private static final String PSINFO_TEMPLATE = "psinfo";

    private static final String SCRIPTS_UNIX_TEMPLATES = "ScriptsUnixTemplates";

    private final Map<String, Object> args;

    private final Threads threads;

    private final Object parent;

    private final Set<ProcessState> processStates;

    @Inject
    private ProcessInfoLogger log;

    @Inject
    private ScriptExecFactory scriptExecFactory;

    @Inject
    private ProcessStateFormatFactory stateFormatFactory;

    private String user;

    private int userId;

    private String group;

    private int groupId;

    private int processId;

    private String commandName;

    private TemplateResource templateResource;

    private boolean found;

    private String commandArgs;

    /**
     * @see ProcessInfoFactory#create(Map, Object, Threads)
     */
    @Inject
    ProcessInfo(@Assisted Map<String, Object> args, @Assisted Object parent,
            @Assisted Threads threads) {
        this.args = args;
        this.parent = parent;
        this.threads = threads;
        this.processStates = new HashSet<ProcessState>();
    }

    @Inject
    public void setTemplatesFactory(TemplatesFactory templatesFactory) {
        Templates templates = templatesFactory.create(SCRIPTS_UNIX_TEMPLATES);
        this.templateResource = templates.getResource(PSINFO_TEMPLATE);
    }

    @Override
    public ProcessInfo call() throws Exception {
        log.checkCommand(args, parent);
        log.checkSearch(args, parent);
        String line = findProcess();
        if (line == null) {
            this.found = false;
            return this;
        } else {
            this.found = true;
            parseScriptOutput(line);
            return this;
        }
    }

    private String findProcess() throws Exception {
        args.put("outString", true);
        String search = args.get(SEARCH).toString();
        ProcessTask script = scriptExecFactory.create(args, parent, threads,
                templateResource, "unix").call();
        return searchProcess(script.getOut(), search);
    }

    private String searchProcess(String string, String search) {
        for (String s : split(string, '\n')) {
            if (s.matches(search)) {
                return s;
            }
        }
        return null;
    }

    private void parseScriptOutput(String string) throws ScriptException {
        String[] out = split(string);
        parseProcessStates(processStates, out[0]);
        this.user = out[1];
        this.group = out[2];
        this.userId = Integer.valueOf(out[3]);
        this.groupId = Integer.valueOf(out[4]);
        this.processId = Integer.valueOf(out[5]);
        this.commandName = out[6];
        if (out.length > 7) {
            this.commandArgs = join(out, ' ', 7, out.length);
        }
    }

    private void parseProcessStates(Set<ProcessState> states, String string)
            throws ScriptException {
        try {
            states.addAll(stateFormatFactory.create().parse(string));
        } catch (ParseException e) {
            throw log.errorParseProcessStates(e, parent);
        }
    }

    public Set<ProcessState> getProcessStates() {
        return unmodifiableSet(processStates);
    }

    public String getProcessUser() {
        return user;
    }

    public int getProcessUserId() {
        return userId;
    }

    public String getProcessGroup() {
        return group;
    }

    public int getProcessGroupId() {
        return groupId;
    }

    public int getProcessId() {
        return processId;
    }

    public String getProcessCommandName() {
        return commandName;
    }

    public boolean isProcessFound() {
        return found;
    }

    public String getProcessCommandArgs() {
        return commandArgs;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).toString();
    }

}
