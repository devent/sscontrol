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
package com.anrisoftware.sscontrol.httpd.statements.authldap;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.startsWith;

import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.inject.assistedinject.Assisted;

/**
 * LDAP/authentication host.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class AuthHost {

	private static final String URL = "url";

	private AuthHostLogger log;

	private Map<String, Object> args;

	private final String name;

	private String url;

	@Inject
	AuthHost(@Assisted Map<String, Object> args, @Assisted String name) {
		this.name = name;
		this.args = args;
	}

	@Inject
	void setAuthHostLogger(AuthHostLogger logger) {
		this.log = logger;
		if (args.containsKey(URL)) {
			setUrl(args.get(URL));
		}
		args = null;
	}

	public String getName() {
		return name;
	}

	public void setUrl(Object url) {
		log.checkUrl(url);
		String string = url.toString();
		this.url = !startsWith(string, "/") ? format("/%s", string) : string;
	}

	public String getUrl() {
		return url;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append(name).append("url", url)
				.toString();
	}
}
