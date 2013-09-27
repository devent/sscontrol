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

import java.util.Map;

/**
 * Factory to create debug logging.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface DebugLoggingFactory {

	/**
	 * Creates debug logging.
	 * 
	 * @param args
	 *            the {@link Map} containing the logging arguments:
	 *            <ul>
	 *            <li>{@code logging:} the logging level.
	 *            </ul>
	 * 
	 * @return the {@link DebugLogging}.
	 */
	DebugLogging create(Map<String, Object> args);

	/**
	 * Creates the debug logging with the specified logging level.
	 * 
	 * @param level
	 *            the logging level.
	 * 
	 * @return the {@link DebugLogging}.
	 */
	DebugLogging create(int level);

	/**
	 * Creates deactivated debug logging.
	 */
	DebugLogging createOff();

}
