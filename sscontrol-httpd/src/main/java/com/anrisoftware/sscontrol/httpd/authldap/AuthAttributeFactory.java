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

/**
 * Factory to creates the LDAP/group attribute.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface AuthAttributeFactory {

	/**
	 * Creates the LDAP/group attribute.
	 * 
	 * @param args
	 *            the arguments {@link Map}:
	 * 
	 * @param name
	 *            the attributes {@link String} name.
	 * 
	 * @return the {@link AuthAttribute}.
	 */
	AuthAttribute create(Map<String, Object> args, String name);
}
