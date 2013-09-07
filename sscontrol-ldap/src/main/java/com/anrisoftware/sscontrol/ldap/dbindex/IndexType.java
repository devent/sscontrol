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
