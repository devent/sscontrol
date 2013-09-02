/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-apache.
 *
 * sscontrol-httpd-apache is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-apache is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-apache. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.apache.linux;

import static com.anrisoftware.sscontrol.httpd.apache.linux.SslDomainConfigLogger._.deployed_cert;
import static com.anrisoftware.sscontrol.httpd.apache.linux.SslDomainConfigLogger._.deployed_cert1;
import static com.anrisoftware.sscontrol.httpd.apache.linux.SslDomainConfigLogger._.deployed_cert_key;
import static com.anrisoftware.sscontrol.httpd.apache.linux.SslDomainConfigLogger._.deployed_cert_key1;
import static com.anrisoftware.sscontrol.httpd.apache.linux.SslDomainConfigLogger._.enabled_ssl_debug;
import static com.anrisoftware.sscontrol.httpd.apache.linux.SslDomainConfigLogger._.enabled_ssl_info;

import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.httpd.statements.domain.SslDomain;

/**
 * Logging messages for {@link SslDomainConfig}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class SslDomainConfigLogger extends AbstractLogger {

	enum _ {

		deployed_cert("Deployed certificate for {}."),

		deployed_cert1("Deployed certificate '{}' for domain '{}'."),

		deployed_cert_key("Deployed certificate key for {}."),

		deployed_cert_key1("Deployed certificate key '{}' for domain '{}'."),

		enabled_ssl_debug("Enabled SSL/mod, {}."),

		enabled_ssl_info("Enabled SSL/mod.");

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
	 * Creates a logger for {@link SslDomainConfig}.
	 */
	public SslDomainConfigLogger() {
		super(SslDomainConfig.class);
	}

	void deployedCert(SslDomain domain) {
		if (isDebugEnabled()) {
			debug(deployed_cert, domain);
		} else {
			info(deployed_cert1, domain.getCertificationFile(),
					domain.getName());
		}
	}

	void deployedCertKey(SslDomain domain) {
		if (isDebugEnabled()) {
			debug(deployed_cert_key, domain);
		} else {
			info(deployed_cert_key1, domain.getCertificationKeyFile(),
					domain.getName());
		}
	}

	void enabledSsl(Object worker) {
		if (isDebugEnabled()) {
			debug(enabled_ssl_debug, worker);
		} else {
			info(enabled_ssl_info);
		}
	}
}
