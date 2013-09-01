package com.anrisoftware.sscontrol.httpd.statements.auth;

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
	file("file");

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
			if (provider.getName().equals(string)) {
				return provider;
			}
		}
		throw new IllegalArgumentException(format(VALID_PROVIDER, string));
	}

	private String name;

	private AuthProvider(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
