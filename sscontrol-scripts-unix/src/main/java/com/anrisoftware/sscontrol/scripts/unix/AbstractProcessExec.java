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

import static java.util.concurrent.TimeUnit.MILLISECONDS;

import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import javax.inject.Inject;

import org.joda.time.Duration;
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

/**
 * Executes a script.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public abstract class AbstractProcessExec implements Callable<ProcessTask> {

    public static final String TIMEOUT = "timeout";

    public static final String DESTROY_ON_TIMEOUT = "destroyOnTimeout";

    public static final String EXIT_CODES = "exitCodes";

    public static final String EXIT_CODE = "exitCode";

    private final Map<String, Object> args;

    private final Threads threads;

    private final Integer exitCode;

    private final int[] exitCodes;

    private final Boolean destroyOnTimeout;

    private final Duration timeout;

    @Inject
    private AbstractProcessExecLogger log;

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

    private String scriptString;

    /**
     * Sets the threads pool and the arguments.
     * 
     * @param threads
     *            the {@link Threads} threads pool.
     * 
     * @param args
     *            the {@link Map} arguments.
     */
    protected AbstractProcessExec(Threads threads, Map<String, Object> args) {
        this.args = args;
        this.threads = threads;
        this.exitCode = getArg(EXIT_CODE, args);
        this.exitCodes = getArg(EXIT_CODES, args);
        this.destroyOnTimeout = getArg(DESTROY_ON_TIMEOUT, args);
        this.timeout = getTimeout(args);
    }

    private Duration getTimeout(Map<String, Object> args2) {
        Duration timeout = getArg(TIMEOUT, args);
        if (timeout == null) {
            timeout = Duration.standardSeconds(60);
        }
        return timeout;
    }

    @SuppressWarnings("unchecked")
    private <T> T getArg(String name, Map<String, Object> args) {
        return (T) (args.containsKey(name) ? args.get(name) : null);
    }

    @Override
    public ProcessTask call() throws Exception {
        log.checkArgs(this, args);
        final CommandLine line = createLine(lineFactory);
        ScriptCommandExec script = createExec();
        script.setObserver(new Observer() {

            @Override
            public void update(Observable o, Object arg) {
                logExecutedScript(line);
            }

        });
        ProcessTask task = exec(line, script);
        return task;
    }

    /**
     * Returns the script that was executed.
     * 
     * @return the script {@link String}.
     */
    public String getScriptString() {
        return scriptString;
    }

    /**
     * Creates the command line for the process.
     * 
     * @param commandLineFactory
     *            the {@link ScriptCommandLineFactory}.
     * 
     * @return the {@link CommandLine}.
     */
    protected abstract CommandLine createLine(
            ScriptCommandLineFactory commandLineFactory);

    private ScriptCommandExec createExec() {
        ScriptCommandExec script = scriptExecFactory.create(execFactory);
        script.setThreads(threads);
        if (exitCode != null) {
            script.setExitCode(exitCode);
        }
        if (exitCodes != null) {
            script.setExitCode(exitCodes);
        }
        if (destroyOnTimeout != null) {
            script.setDestroyOnTimeout(destroyOnTimeout);
        }
        return script;
    }

    private ProcessTask exec(CommandLine line, ScriptCommandExec script)
            throws Exception {
        Logger logger = (Logger) args.get("log");
        script.setCommandError(errorOutputFactory.create(logger, line));
        script.setCommandOutput(debugOutputFactory.create(logger, line));
        Future<ProcessTask> future = script.exec(line);
        if (timeout != null) {
            return future.get(timeout.getMillis(), MILLISECONDS);
        } else {
            return future.get();
        }
    }

    private void logExecutedScript(CommandLine line) {
        String string = line.getExecutable().toString();
        log.executedScript(this, string);
        this.scriptString = string;
    }
}
