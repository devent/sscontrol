/*
 * Copyright 2012-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-workers-command.
 *
 * sscontrol-workers-command is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-workers-command is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-workers-command. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.workers.command.exec;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteStreamHandler;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.workers.api.Worker;
import com.anrisoftware.sscontrol.workers.api.WorkerException;
import com.anrisoftware.sscontrol.workers.command.utils.Nullable;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * Execute a shell command with the use of {@link ExecHelper}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class ExecCommandWorker implements Worker {

    private static final String QUOTATION = "quotation";

    private static final String TIME_OUT = "time out [ms]";

    private static final String ENVIRONMENT = "environment";

    private static final String COMMAND = "command";

    /**
     * The default timeout time in milliseconds.
     */
    public static final int DEFAULT_TIMEOUT_MS = 60 * 1000;

    private final ExecCommandWorkerLogger log;

    private final String command;

    private final Map<String, String> environment;

    private int exitCode;

    private transient ByteArrayOutputStream out;

    private transient ByteArrayOutputStream err;

    private long timeoutMs;

    private boolean quotation;

    private int[] exitValues;

    /**
     * @see ExecCommandWorkerFactory#create(String)
     */
    @AssistedInject
    ExecCommandWorker(ExecCommandWorkerLogger logger, @Assisted String command) {
        this(logger, command, new HashMap<String, String>(), DEFAULT_TIMEOUT_MS);
    }

    /**
     * @see ExecCommandWorkerFactory#create(String, Map)
     */
    @AssistedInject
    ExecCommandWorker(ExecCommandWorkerLogger logger, @Assisted String command,
            @Assisted @Nullable Map<String, String> environment) {
        this(logger, command, environment, DEFAULT_TIMEOUT_MS);
    }

    /**
     * @see ExecCommandWorkerFactory#create(String, Map, long)
     */
    @AssistedInject
    ExecCommandWorker(ExecCommandWorkerLogger logger, @Assisted String command,
            @Assisted @Nullable Map<String, String> environment,
            @Assisted long timeoutMs) {
        this.log = logger;
        this.environment = createEnvironment(environment);
        this.command = command;
        this.timeoutMs = timeoutMs;
        this.quotation = true;
        setExitValue(0);
        readResolve();
    }

    private Map<String, String> createEnvironment(
            Map<String, String> environment) {
        if (environment != null) {
            return new HashMap<String, String>(environment);
        } else {
            return null;
        }
    }

    private Object readResolve() {
        this.out = new ByteArrayOutputStream();
        this.err = new ByteArrayOutputStream();
        return this;
    }

    @Override
    public ExecCommandWorker call() throws WorkerException {
        CommandLine cmdline = CommandLine.parse(command);
        cmdline = quotation ? cmdline : new CommandLineWithoutQuote(cmdline);
        ExecuteStreamHandler streamHandler = new PumpStreamHandler(out, err);
        DefaultExecutor executor = new DefaultExecutor();
        executor.setExitValues(exitValues);
        ExecuteWatchdog watchdog = new ExecuteWatchdog(timeoutMs);
        executor.setWatchdog(watchdog);
        executor.setStreamHandler(streamHandler);
        startProcess(executor, cmdline);
        log.finishedProcess(this);
        return this;
    }

    private void startProcess(DefaultExecutor executor, CommandLine cmdline)
            throws WorkerException {
        try {
            log.startProcess(this);
            exitCode = executor.execute(cmdline, environment);
        } catch (IOException e) {
            throw log.errorExecuteCommand(this, e);
        }
    }

    /**
     * Sets if the parameter should be quoted in double quotes or not.
     * 
     * @param haveQuotation
     *            set to {@code true} to automatically quote arguments in double
     *            quotes or to {@code false} if not.
     */
    public void setQuotation(boolean haveQuotation) {
        quotation = haveQuotation;
    }

    public Map<String, String> getEnvironment() {
        return environment;
    }

    public void setTimeoutMs(long newTimeoutMs) {
        timeoutMs = newTimeoutMs;
    }

    public void setExitValue(int value) {
        this.exitValues = new int[] { value };
    }

    /**
     * Sets the list of valid exit values for the process.
     * 
     * @param values
     *            the integer array of exit values or {@code null} to skip
     *            checking of exit codes.
     * 
     * @see Executor#setExitValues(int[])
     */
    public void setExitValues(int[] values) {
        this.exitValues = values;
    }

    public int getExitCode() {
        return exitCode;
    }

    public String getOut() {
        return out.toString();
    }

    public String getOut(String charsetName)
            throws UnsupportedEncodingException {
        return out.toString(charsetName);
    }

    public String getErr() {
        return err.toString();
    }

    public String getErr(String charsetName)
            throws UnsupportedEncodingException {
        return err.toString(charsetName);
    }

    public String getCommand() {
        return command;
    }

    public String getCommandName() {
        return StringUtils.split(command, " ")[0];
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this);
        builder.append(COMMAND, command);
        builder.append(TIME_OUT, timeoutMs);
        builder.append(QUOTATION, quotation);
        if (environment != null) {
            builder.append(ENVIRONMENT, environment);
        }
        return builder.toString();
    }
}
