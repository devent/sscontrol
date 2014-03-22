/*
 * Copyright 2012-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-ldap.
 *
 * sscontrol-ldap is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-ldap is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-ldap. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.ldap.statements;

import java.util.Map;

/**
 * Factory to create the administrator.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface AdminFactory {

	/**
	 * Creates the administrator.
	 * 
	 * @param args
	 *            the {@link Map} arguments:
	 *            <ul>
	 *            <li>{@code password:} the password.
	 *            <li>{@code description:} the description of the administrator
	 *            user.
	 *            </ul>
	 * 
	 * @param domain
	 *            the {@link DomainComponent}.
	 * 
	 * @param name
	 *            the administrator user {@link String} name.
	 * 
	 * @return the {@link Admin}.
	 */
	Admin create(Map<String, Object> args, DomainComponent domain, String name);
}
