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
