package com.anrisoftware.sscontrol.httpd.statements.authldap;

import static com.anrisoftware.sscontrol.httpd.statements.authldap.AuthAttributeLogger._.attribute_name_null;
import static org.apache.commons.lang3.Validate.notBlank;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link AuthAttribute}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class AuthAttributeLogger extends AbstractLogger {

	enum _ {

		attribute_name_null("Attribute name cannot be null or blank.");

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
	 * Creates a logger for {@link AuthAttribute}.
	 */
	public AuthAttributeLogger() {
		super(AuthAttribute.class);
	}

	void checkAttribute(String name) {
		notBlank(name, attribute_name_null.toString());
	}

}
