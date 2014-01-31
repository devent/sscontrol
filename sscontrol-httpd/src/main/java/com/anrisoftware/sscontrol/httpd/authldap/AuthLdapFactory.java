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
package com.anrisoftware.sscontrol.httpd.authldap;

import java.util.Map;

import com.anrisoftware.sscontrol.httpd.auth.AuthProvider;
import com.anrisoftware.sscontrol.httpd.auth.AuthType;

/**
 * Factory to create LDAP/authentication.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface AuthLdapFactory {

	/**
	 * Create the authentication.
	 * 
	 * @param args
	 *            {@link Map} of the arguments:
	 *            <ul>
	 *            <li>{@code location} the location of the resource;
	 *            <li>{@code type} the {@link AuthType} type of the
	 *            authentication;
	 *            <li>{@code provider} the {@link AuthProvider} provider of the
	 *            authentication;
	 *            <li>{@code satisfy} the {@link AuthProvider} provider of the
	 *            resource;
	 *            </ul>
	 * 
	 * @param name
	 *            the name {@link String}.
	 * 
	 * @return the created {@link AuthLdap} authentication.
	 */
	AuthLdap create(Map<String, Object> args, String name);
}
