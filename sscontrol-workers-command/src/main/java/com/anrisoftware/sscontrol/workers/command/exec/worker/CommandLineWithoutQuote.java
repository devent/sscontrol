package com.anrisoftware.sscontrol.workers.command.exec.worker;

import org.apache.commons.exec.CommandLine;

/**
 * Removes the double quotes around an argument.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
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
