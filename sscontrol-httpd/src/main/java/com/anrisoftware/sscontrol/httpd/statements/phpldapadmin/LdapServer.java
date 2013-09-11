/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd.
 *
 * sscontrol-httpd is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.statements.phpldapadmin;

import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.httpd.statements.ports.ServicePort;
import com.google.inject.assistedinject.Assisted;

/**
 * LDAP/server.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class LdapServer {

	private static final String PORT = "port";

	private static final String HOST = "host";

	private final LdapServerLogger log;

	private final String name;

	private String host;

	private Integer port;

	/**
	 * @see LdapServerFactory#create(Map, String)
	 */
	@Inject
	LdapServer(LdapServerLogger logger, @Assisted Map<String, Object> args,
			@Assisted String name) {
		this.log = logger;
		this.name = name;
		this.port = null;
		setHost(args.get(HOST));
		if (args.containsKey(PORT)) {
			setPort(args.get(PORT));
		}
	}

	public String getName() {
		return name;
	}

	private void setHost(Object object) {
		log.checkHost(object);
		setHost(object.toString());
	}

	public void setHost(String host) {
		log.checkHost(host);
		this.host = host;
	}

	public String getHost() {
		return host;
	}

	private void setPort(Object object) {
		if (object instanceof Number) {
			Number number = (Number) object;
			setPort(number.intValue());
		} else {
			setPort(ServicePort.valueOf(object.toString()).getPort());
		}
	}

	public void setPort(int port) {
		this.port = port;
	}

	public Integer getPort() {
		return port;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append(name).append("host", host)
				.append("port", port).toString();
	}
}
