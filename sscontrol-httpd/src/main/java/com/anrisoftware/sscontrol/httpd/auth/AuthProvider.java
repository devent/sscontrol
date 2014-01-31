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
package com.anrisoftware.sscontrol.httpd.auth;

import static java.lang.String.format;

/**
 * Different authentication provider.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public enum AuthProvider {

	/**
	 * Authentication using text files.
	 */
	file,

	/**
	 * Authentication using LDAP/server.
	 */
	ldap;

	private static final String VALID_PROVIDER = "String '%s' is not a valid provider.";

	/**
	 * Parses the string to an authentication provider.
	 * 
	 * @param string
	 *            the {@link String} to parse.
	 * 
	 * @return the {@link AuthProvider}.
	 * 
	 * @throws IllegalArgumentException
	 *             if the specified string is not a valid provider.
	 */
	public static AuthProvider parse(String string) {
		for (AuthProvider provider : values()) {
			if (provider.toString().equals(string)) {
				return provider;
			}
		}
		throw new IllegalArgumentException(format(VALID_PROVIDER, string));
	}
}
