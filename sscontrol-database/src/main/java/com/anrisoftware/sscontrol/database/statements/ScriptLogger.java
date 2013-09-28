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
package com.anrisoftware.sscontrol.database.statements;

import static com.anrisoftware.sscontrol.database.statements.ScriptLogger._.invalid_syntax;
import static com.anrisoftware.sscontrol.database.statements.ScriptLogger._.invalid_syntax_message;
import static com.anrisoftware.sscontrol.database.statements.ScriptLogger._.invalid_url;
import static com.anrisoftware.sscontrol.database.statements.ScriptLogger._.invalid_url_message;
import static com.anrisoftware.sscontrol.database.statements.ScriptLogger._.path_absoulte;
import static com.anrisoftware.sscontrol.database.statements.ScriptLogger._.path_absoulte_message;
import static com.anrisoftware.sscontrol.database.statements.ScriptLogger._.resource_null;
import static org.apache.commons.lang3.Validate.notNull;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.api.ServiceException;

/**
 * Logging messages for {@link Script}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class ScriptLogger extends AbstractLogger {

	enum _ {

		resource_null("Script resource cannot be null."),

		invalid_syntax("Invalid URI syntax"),

		invalid_syntax_message("Invalid URI syntax for '{}'."),

		path("path"),

		invalid_url("Invalid URL"),

		invalid_url_message("Invalid URL for '{}'."),

		path_absoulte("Path is not absolute"),

		path_absoulte_message("Path is not absolute: '{}'.");

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
	 * Create logger for {@link Script}.
	 */
	ScriptLogger() {
		super(Script.class);
	}

	void checkResource(Object object) {
		notNull(object, resource_null.toString());
	}

	ServiceException invalidUri(URISyntaxException e, Object path) {
		return logException(
				new ServiceException(invalid_syntax, e).add(_.path, path),
				invalid_syntax_message, path);
	}

	ServiceException invalidUrl(MalformedURLException e, Object path) {
		return logException(
				new ServiceException(invalid_url, e).add(_.path, path),
				invalid_url_message, path);
	}

	ServiceException pathNotAbsolute(IllegalArgumentException e, String path) {
		return logException(
				new ServiceException(path_absoulte, e).add(_.path, path),
				path_absoulte_message, path);
	}
}
