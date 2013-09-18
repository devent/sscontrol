/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-mail.
 *
 * sscontrol-mail is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-mail is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-mail. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.mail.debuglogging;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Debug logging level.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class DebugLoggingLevel extends Number {

	/**
	 * Deactivates all logging.
	 */
	public static final DebugLoggingLevel OFF = new DebugLoggingLevel(0);

	private final int level;

	public DebugLoggingLevel(int level) {
		this.level = level;
	}

	public int getLevel() {
		return level;
	}

	@Override
	public int intValue() {
		return level;
	}

	@Override
	public long longValue() {
		return level;
	}

	@Override
	public float floatValue() {
		return level;
	}

	@Override
	public double doubleValue() {
		return level;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).toString();
	}
}
