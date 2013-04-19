/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
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
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.workers.api.Worker;
import com.anrisoftware.sscontrol.workers.api.WorkerException;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * Execute a shell command with the use of {@link ExecHelper}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class ExecCommandWorker implements Worker {

	/**
	 * @since 1.0
	 */
	private static final long serialVersionUID = -2471955539937001960L;

	/**
	 * The default timeout time in milliseconds.
	 */
	public static final int DEFAULT_TIMEOUT_MS = 60 * 1000;

	private final ExecCommandWorkerLogger log;

	private final String command;

	private final HashMap<String, String> environment;

	private int exitCode;

	private transient ByteArrayOutputStream out;

	private transient ByteArrayOutputStream err;

	private long timeoutMs;

	private String output;

	private String error;

	private boolean quatation;

	private int[] exitValues;

	@AssistedInject
	ExecCommandWorker(ExecCommandWorkerLogger logger, @Assisted String command) {
		this(logger, command, new HashMap<String, String>(), DEFAULT_TIMEOUT_MS);
	}

	@AssistedInject
	ExecCommandWorker(ExecCommandWorkerLogger logger, @Assisted String command,
			@Assisted Map<String, String> environment) {
		this(logger, command, environment, DEFAULT_TIMEOUT_MS);
	}

	@AssistedInject
	ExecCommandWorker(ExecCommandWorkerLogger logger, @Assisted String command,
			@Assisted Map<String, String> environment, @Assisted long timeoutMs) {
		this.log = logger;
		this.environment = new HashMap<String, String>(environment);
		this.command = command;
		this.timeoutMs = timeoutMs;
		this.quatation = true;
		setExitValue(0);
		readResolve();
	}

	private Object readResolve() {
		this.out = new ByteArrayOutputStream();
		this.err = new ByteArrayOutputStream();
		return this;
	}

	@Override
	public ExecCommandWorker call() throws WorkerException {
		CommandLine cmdline = CommandLine.parse(command);
		cmdline = quatation ? cmdline : new CommandLineWithoutQuote(cmdline);
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
		quatation = haveQuotation;
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
		if (output == null) {
			output = out.toString();
		}
		return output;
	}

	public String getOut(String charsetName)
			throws UnsupportedEncodingException {
		if (output == null) {
			output = out.toString(charsetName);
		}
		return output;
	}

	public String getErr() {
		if (error == null) {
			error = err.toString();
		}
		return error;
	}

	public String getErr(String charsetName)
			throws UnsupportedEncodingException {
		if (error == null) {
			error = err.toString(charsetName);
		}
		return error;
	}

	public String getCommand() {
		return command;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append(command).append(environment)
				.toString();
	}
}
