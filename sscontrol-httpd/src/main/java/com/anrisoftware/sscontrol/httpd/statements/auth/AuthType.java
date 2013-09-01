package com.anrisoftware.sscontrol.httpd.statements.auth;

import static java.lang.String.format;

/**
 * Different authentication types.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public enum AuthType {

	/**
	 * MD5 digest authentication.
	 */
	digest("digest"),

	/**
	 * HTTP basic authentication.
	 */
	basic("basic");

	private static final String VALID_TYPE = "String '%s' is not a valid provider.";

	/**
	 * Parses the string to an authentication type.
	 * 
	 * @param string
	 *            the {@link String} to parse.
	 * 
	 * @return the {@link AuthType}.
	 * 
	 * @throws IllegalArgumentException
	 *             if the specified string is not a valid type.
	 */
	public static AuthType parse(String string) {
		for (AuthType type : values()) {
			if (type.getName().equals(string)) {
				return type;
			}
		}
		throw new IllegalArgumentException(format(VALID_TYPE, string));
	}

	private String name;

	private AuthType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
