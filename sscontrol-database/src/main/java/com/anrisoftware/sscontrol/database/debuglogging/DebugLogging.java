/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-database.
 *
 * sscontrol-database is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-database is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-database. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.database.debuglogging;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * Debug logging level.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class DebugLogging extends Number {

	private static final String LOGGING = "logging";

	private static final String LEVEL = "level";

	private DebugLoggingLogger log;

	private final Map<String, Object> args;

	@AssistedInject
	DebugLogging() {
		this(0);
	}

	@AssistedInject
	DebugLogging(@Assisted int level) {
		this(new HashMap<String, Object>());
		args.put(LEVEL, level);
	}

	@AssistedInject
	DebugLogging(@Assisted Map<String, Object> args) {
		this.args = args;
	}

	@Inject
	void setDebugLoggingLogger(DebugLoggingLogger logger) {
		this.log = logger;
		setLevel(args.get(LOGGING));
	}

	private void setLevel(Object object) {
		log.checkLevel(object);
		args.put(LEVEL, object);
	}

	public int getLevel() {
		return (Integer) args.get(LEVEL);
	}

	public Map<String, Object> getArgs() {
		return args;
	}

	@Override
	public int intValue() {
		return getLevel();
	}

	@Override
	public long longValue() {
		return getLevel();
	}

	@Override
	public float floatValue() {
		return getLevel();
	}

	@Override
	public double doubleValue() {
		return getLevel();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append(args).toString();
	}
}
