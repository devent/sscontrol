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
 * @since 0.1
 */
public class ExecCommandWorker implements Worker {

	/**
	 * @version 0.1
	 */
	private static final long serialVersionUID = -2471955539937001960L;

	/**
	 * The default timeout time in milliseconds.
	 */
	public static final int DEFAULT_TIMEOUT_MS = 60 * 1000;

	private final ExecCommandWorkerLogger log;

	private final String command;

	private final HashMap<String, String> environment;

	private int exitValue;

	private int exitCode;

	private transient final ByteArrayOutputStream out;

	private transient final ByteArrayOutputStream err;

	private long timeoutMs;

	private String output;

	private String error;

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
		this.exitValue = 0;
		this.timeoutMs = timeoutMs;
		this.out = new ByteArrayOutputStream();
		this.err = new ByteArrayOutputStream();
	}

	@Override
	public ExecCommandWorker call() throws WorkerException {
		CommandLine cmdline = CommandLine.parse(command);
		ExecuteStreamHandler streamHandler = new PumpStreamHandler(out, err);
		DefaultExecutor executor = new DefaultExecutor();
		executor.setExitValue(exitValue);
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
			exitCode = executor.execute(cmdline, environment);
		} catch (IOException e) {
			throw log.errorExecuteCommand(this, e);
		}
	}

	public Map<String, String> getEnvironment() {
		return environment;
	}

	public void setTimeoutMs(long newTimeoutMs) {
		timeoutMs = newTimeoutMs;
	}

	public void setExitValue(int value) {
		exitValue = value;
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

	@Override
	public String toString() {
		return new ToStringBuilder(this).append(command).append(environment)
				.toString();
	}
}
