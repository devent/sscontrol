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
 * Installs specified packages on the system.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class InstallPackages implements Callable<ProcessTask> {

    private final Map<String, Object> args;

    private final InstallPackagesLogger log;

    private final Object parent;

    private final Threads threads;

    @Inject
    private CommandTemplatesProvider commandTemplates;

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

    private String system;

    /**
     * @see InstallPackagesFactory#create(Map, Object, Threads)
     */
    @Inject
    InstallPackages(InstallPackagesLogger log, @Assisted Object parent,
            @Assisted Threads threads, @Assisted Map<String, Object> args) {
        this.log = log;
        this.parent = parent;
        this.args = args;
        this.threads = threads;
        log.checkArgs(args);
        this.system = "unix";
    }

    public void setSystem(String system) {
        this.system = system;
    }

    @Override
    public ProcessTask call() throws Exception {
        TemplateResource template = getTemplate();
        CommandLine line = createLine(template);
        ScriptCommandExec script = createExec();
        ProcessTask task = exec(line, script);
        log.installPackagesDone(parent, task, args);
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
        return lineFactory.create(system, template).addSub("args", args);
    }

    private TemplateResource getTemplate() {
        return commandTemplates.get().getResource("install");
    }
}
