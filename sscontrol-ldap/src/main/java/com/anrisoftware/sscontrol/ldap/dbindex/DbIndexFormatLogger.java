/*
 * Copyright 2012-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-ldap.
 *
 * sscontrol-ldap is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-ldap is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-ldap. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.ldap.dbindex;

import static com.anrisoftware.sscontrol.ldap.dbindex.DbIndexFormatLogger._.error_parse;
import static com.anrisoftware.sscontrol.ldap.dbindex.DbIndexFormatLogger._.error_parse1;
import static java.lang.String.format;

import java.text.ParseException;
import java.text.ParsePosition;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link DbIndexFormat}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class DbIndexFormatLogger extends AbstractLogger {

	enum _ {

		error_parse("Error parse source as database index"),

		error_parse1("Error parse source '{}' as database index.");

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
	 * Creates a logger for {@link DbIndexFormat}.
	 */
	public DbIndexFormatLogger() {
		super(DbIndexFormat.class);
	}

	ParseException errorParseIndex(String source, ParsePosition pos) {
		return logException(
				new ParseException(format(error_parse.toString(), source),
						pos.getErrorIndex()), error_parse1, source);
	}

}
