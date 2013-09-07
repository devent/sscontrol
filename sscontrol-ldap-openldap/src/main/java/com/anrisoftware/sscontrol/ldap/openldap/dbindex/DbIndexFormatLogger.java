package com.anrisoftware.sscontrol.ldap.openldap.dbindex;

import static com.anrisoftware.sscontrol.ldap.openldap.dbindex.DbIndexFormatLogger._.error_parse;
import static com.anrisoftware.sscontrol.ldap.openldap.dbindex.DbIndexFormatLogger._.error_parse1;
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
