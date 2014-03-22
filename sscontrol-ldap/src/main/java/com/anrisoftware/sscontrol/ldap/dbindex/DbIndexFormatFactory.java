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
package com.anrisoftware.sscontrol.ldap.dbindex;

/**
 * Factory to create the database index format.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface DbIndexFormatFactory {

	/**
	 * Creates the database index format.
	 * 
	 * @return the {@link DbIndexFormat}.
	 */
	DbIndexFormat create();

	/**
	 * Creates the database index format.
	 * 
	 * @param separator
	 *            the {@link String} to separate the names and types.
	 * 
	 * @return the {@link DbIndexFormat}.
	 */
	DbIndexFormat create(String separator);
}
