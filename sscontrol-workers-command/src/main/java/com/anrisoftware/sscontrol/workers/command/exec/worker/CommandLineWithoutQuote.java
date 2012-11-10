/*
 * Copyright 2012 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.workers.command.exec.worker;

import org.apache.commons.exec.CommandLine;

/**
 * Removes the double quotes around an argument.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class CommandLineWithoutQuote extends CommandLine {

	/**
	 * Copy constructor.
	 * 
	 * @param other
	 *            the instance to copy
	 */
	public CommandLineWithoutQuote(CommandLine other) {
		super(other);
	}

	@Override
	public String[] getArguments() {
		String[] args = super.getArguments();
		for (int i = 0; i < args.length; i++) {
			if (args[i].startsWith("\"")) {
				args[i] = args[i].substring(1);
			}
			if (args[i].endsWith("\"")) {
				args[i] = args[i].substring(0, args[i].length() - 1);
			}
		}
		return args;
	}
}
