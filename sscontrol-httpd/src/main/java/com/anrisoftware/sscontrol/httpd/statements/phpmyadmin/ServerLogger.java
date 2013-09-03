package com.anrisoftware.sscontrol.httpd.statements.phpmyadmin;

import static com.anrisoftware.sscontrol.httpd.statements.phpmyadmin.ServerLogger._.host_null;
import static com.anrisoftware.sscontrol.httpd.statements.phpmyadmin.ServerLogger._.port_invalid;
import static com.anrisoftware.sscontrol.httpd.statements.phpmyadmin.ServerLogger._.port_null;
import static org.apache.commons.lang3.Validate.inclusiveBetween;
import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging messages for {@link Server}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class ServerLogger extends AbstractLogger {

	enum _ {

		host_null("Server host cannot be null or blank."),

		port_null("Server port cannot be null."),

		port_invalid("Server port %d invalid.");

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
	 * Creates a logger for {@link Server}.
	 */
	public ServerLogger() {
		super(Server.class);
	}

	void checkHost(String host) {
		notBlank(host, host_null.toString());
	}

	void checkPort(Integer port) {
		notNull(port, port_null.toString());
		inclusiveBetween(1, 65535, port, port_invalid.toString(), port);
	}
}
