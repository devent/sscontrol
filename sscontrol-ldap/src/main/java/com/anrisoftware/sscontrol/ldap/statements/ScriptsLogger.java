/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.ldap.statements;

import static com.anrisoftware.sscontrol.ldap.statements.ScriptsLogger._.charsetNull;
import static com.anrisoftware.sscontrol.ldap.statements.ScriptsLogger._.read_text_error;
import static com.anrisoftware.sscontrol.ldap.statements.ScriptsLogger._.read_text_error_message;
import static com.anrisoftware.sscontrol.ldap.statements.ScriptsLogger._.resourceNull;
import static org.apache.commons.lang3.Validate.notNull;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link Scripts}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class ScriptsLogger extends AbstractLogger {

	enum _ {

		charsetNull("Character set cannot be null."),

		read_text_error("Read script resource error"),

		resource("resource"),

		read_text_error_message("Read script resource '{}' error"),

		resourceNull("Resource cannot be null.");

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

	void checkCharset(Charset charset) {
		notNull(charset, charsetNull.toString());
	}

	ScriptException readTextError(IOException e, URI resource) {
		return logException(
				new ScriptException(read_text_error, e).add(resource, resource),
				read_text_error_message, resource);
	}

	void checkResource(Object resource) {
		notNull(resource, resourceNull.toString());
	}

}
