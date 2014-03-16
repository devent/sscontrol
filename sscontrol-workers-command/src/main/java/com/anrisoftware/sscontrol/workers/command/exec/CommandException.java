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

import static java.lang.String.format;

import com.anrisoftware.sscontrol.workers.api.WorkerException;

/**
 * Indicate that some error occurred while executing a command.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class CommandException extends WorkerException {

    private static final String message = "Error execute command '%s', error %d, output:\n>>>\n%s<<<, error output:\n>>>\n%s<<<";

    private final String command;

    private final int error;

    private final String output;

    private final String errorOutput;

    /**
     * @see WorkerException#WorkerException(String, Throwable)
     */
    public CommandException(Throwable cause, String command, int error,
            String out, String errOut) {
        super(format(message, command, error, out, errOut), cause);
        this.command = command;
        this.error = error;
        this.output = out;
        this.errorOutput = errOut;
    }

    public String getCommand() {
        return command;
    }

    public int getError() {
        return error;
    }

    public String getOutput() {
        return output;
    }

    public String getErrorOutput() {
        return errorOutput;
    }

}
