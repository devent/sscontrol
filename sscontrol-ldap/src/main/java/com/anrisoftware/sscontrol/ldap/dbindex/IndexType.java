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

import static java.lang.String.format;

/**
 * Index type.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public enum IndexType {

	present, equality, substring, approx, none;

	private static final String STRING_INVALID = "String '%s' invalid index type";

	/**
	 * Parses the string to database index type.
	 * 
	 * @param str
	 *            the string to parse.
	 * 
	 * @return the parsed {@link IndexType}.
	 * 
	 * @throws IllegalArgumentException
	 *             if the specified string is not a valid index type.
	 */
	public static IndexType parse(String str) {
		for (IndexType type : values()) {
			if (str.equals(type.toString())) {
				return type;
			}
		}
		throw new IllegalArgumentException(format(STRING_INVALID, str));
	}
}
