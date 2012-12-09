/*
 * Copyright 2012 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-workers-text.
 *
 * sscontrol-workers-text is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-workers-text is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-workers-text. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.workers.text.tokentemplate;

import static java.lang.String.format;
import static java.util.regex.Pattern.compile;

import java.io.Serializable;
import java.util.regex.Pattern;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Holds the search text and replacement.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class TokenTemplate implements Serializable {

	/**
	 * @since 1.0
	 */
	private static final long serialVersionUID = 7393785512827671511L;

	private final String search;

	private final String replace;

	private final int flags;

	/**
	 * Sets the search and replacement string.
	 *
	 * @param search
	 *            the search string.
	 *
	 * @param replace
	 *            the replacement string.
	 */
	public TokenTemplate(String search, String replace) {
		this.search = search;
		this.replace = replace;
		this.flags = 0;
	}

	/**
	 * Sets the search and replacement string.
	 *
	 * @param search
	 *            the search string.
	 *
	 * @param replace
	 *            the replacement string.
	 *
	 * @param flags
	 *            Match flags, a bit mask that may include
	 *            <ul>
	 *            <li>{@link Pattern#CASE_INSENSITIVE},
	 *            <li>{@link Pattern#MULTILINE},
	 *            <li>{@link Pattern#DOTALL},
	 *            <li>{@link Pattern#UNICODE_CASE},
	 *            <li>{@link Pattern#CANON_EQ},
	 *            <li>{@link Pattern#UNIX_LINES},
	 *            <li>{@link Pattern#LITERAL},
	 *            <li>{@link Pattern#UNICODE_CHARACTER_CLASS} and
	 *            <li>{@link Pattern#COMMENTS}.
	 *            </ul>
	 */
	public TokenTemplate(String search, String replace, int flags) {
		this.search = search;
		this.replace = replace;
		this.flags = flags;
	}

	/**
	 * Returns the search string.
	 * 
	 * @return the search string
	 */
	public String getSearch() {
		return search;
	}

	/**
	 * Returns the replacement string.
	 *
	 * @return the replacement string
	 */
	public String getReplace() {
		return replace;
	}

	/**
	 * Returns the pattern with the begin and end token.
	 *
	 * @param beginToken
	 *            the begin token.
	 *
	 * @param endToken
	 *            the end token.
	 *
	 * @return the {@link Pattern}.
	 */
	public Pattern toPattern(String beginToken, String endToken) {
		String config = format("(%s)", search);
		String pattern;
		pattern = format("(%s\\n)?%s(\\n%s)?", beginToken, config, endToken);
		return compile(pattern, flags);
	}

	/**
	 * Returns the replace string with the begin and end token.
	 *
	 * @param beginToken
	 *            the begin token.
	 *
	 * @param endToken
	 *            the end token.
	 *
	 * @return the replace string.
	 */
	public String toReplace(String beginToken, String endToken) {
		return format("%s\n%s\n%s", beginToken, replace, endToken);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("search", search)
				.append("replace", replace).toString();
	}

}
