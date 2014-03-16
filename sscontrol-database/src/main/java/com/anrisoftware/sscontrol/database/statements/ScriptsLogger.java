/*
 * Copyright 2012-2014 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.database.statements;

import static com.anrisoftware.sscontrol.database.statements.ScriptsLogger._.error_load_script;
import static com.anrisoftware.sscontrol.database.statements.ScriptsLogger._.error_load_script_message;
import static com.anrisoftware.sscontrol.database.statements.ScriptsLogger._.script_added_debug;
import static com.anrisoftware.sscontrol.database.statements.ScriptsLogger._.script_added_info;
import static java.lang.String.format;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.database.statements.Scripts.ErrorHandler;

/**
 * Logging messages for {@link Scripts}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class ScriptsLogger extends AbstractLogger {

	enum _ {

		script_added_debug("Script resource {} added."),

		script_added_info("Script resource '{}' added."),

		error_load_script("Error load script resource"),

		error_load_script_message("Error load script resource '{}'.");

		private String name;

		private _(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	/**
	 * Creates a logger for {@link Scripts}.
	 */
	public ScriptsLogger() {
		super(Scripts.class);
	}

	void scriptAdd(Script script) {
		if (isDebugEnabled()) {
			debug(script_added_debug, script);
		} else {
			info(script_added_info, script.getResource());
		}
	}

	ErrorHandler getScriptHandler() {
		return new ErrorHandler() {

			@Override
			public void errorThrown(Script script, Exception e) {
				throw logException(
						new RuntimeException(format(
								error_load_script.toString(), script), e),
						error_load_script_message, script.getResource());
			}
		};
	}
}
