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

    /**
     * Default {@code outString} value, set to {@code false}.
     */
    public static final boolean OUT_STRING_DEFAULT = false;

    /**
     * Default {@code destroyOnTimeout} value, set to {@code true}.
     */
    public static final boolean DESTROY_ON_TIMEOUT_DEFAULT = true;

    /**
     * Default {@code exitCode} value, set to {@code 0}.
     */
    public static final int EXIT_CODE_DEFAULT = 0;

    /**
     * Default {@code timeout} duration, set to 60 seconds.
     */
    public static final Duration TIMEOUT_DEFAULT = Duration.standardSeconds(60);

    private static final String LOG_KEY = "log";

    private static final String OUT_STRING = "outString";

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

    private final boolean outString;

    private final boolean checkExitCodes;

    @Inject
    private AbstractProcessExecLogger log;

    @Inject
    private ScriptCommandExecFactory scriptExecFactory;

    @Inject
    private ScriptCommandLineFactory lineFactory;

    @Inject
    private DefaultCommandExecFactory execFactory;

    @Inject
    private DebugLogCommandOutputFactory outputFactory;

    @Inject
    private ErrorLogCommandOutputFactory errorFactory;

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
        this.exitCode = getArg(EXIT_CODE, args, EXIT_CODE_DEFAULT);
        this.exitCodes = getArg(EXIT_CODES, args);
        this.checkExitCodes = getArg("checkExitCodes", args, true);
        this.destroyOnTimeout = getArg(DESTROY_ON_TIMEOUT, args,
                DESTROY_ON_TIMEOUT_DEFAULT);
        this.timeout = getArg(TIMEOUT, args, TIMEOUT_DEFAULT);
        this.outString = getArg(OUT_STRING, args, OUT_STRING_DEFAULT);
    }

    @SuppressWarnings("unchecked")
    private <T> T getArg(String name, Map<String, Object> args) {
        return (T) (args.containsKey(name) ? args.get(name) : null);
    }

    @SuppressWarnings("unchecked")
    private <T> T getArg(String name, Map<String, Object> args, T defaultValue) {
        return (T) (args.containsKey(name) ? args.get(name) : defaultValue);
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
        if (exitCode != null && checkExitCodes) {
            script.setExitCode(exitCode);
        }
        if (exitCodes != null && checkExitCodes) {
            script.setExitCode(exitCodes);
        }
        if (destroyOnTimeout != null) {
            script.setDestroyOnTimeout(destroyOnTimeout);
        }
        return script;
    }

    private ProcessTask exec(CommandLine line, ScriptCommandExec script)
            throws Exception {
        setupCommandError(script, line);
        setupCommandOutput(script, line);
        Future<ProcessTask> future = script.exec(line);
        if (timeout != null) {
            return future.get(timeout.getMillis(), MILLISECONDS);
        } else {
            return future.get();
        }
    }

    protected void setupCommandOutput(ScriptCommandExec script, CommandLine line) {
        if (outString) {
            return;
        }
        Logger logger = (Logger) args.get(LOG_KEY);
        script.setCommandOutput(outputFactory.create(logger, line));
    }

    protected void setupCommandError(ScriptCommandExec script, CommandLine line) {
        Logger logger = (Logger) args.get(LOG_KEY);
        script.setCommandError(errorFactory.create(logger, line));
    }

    private void logExecutedScript(CommandLine line) {
        String string = line.getExecutable().toString();
        log.executedScript(this, string);
        this.scriptString = string;
    }
}
