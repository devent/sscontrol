/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
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

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Wraps the begin and end token of the place holders.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class TokenMarker {

	/**
	 * The {@link String} token that marks the beginning of the template
	 * parameter.
	 */
	public final String beginToken;

	/**
	 * The {@link String} token that marks the ending of the template parameter.
	 */
	public final String endToken;

	/**
	 * Sets the begin and end token.
	 * 
	 * @param beginToken
	 *            the {@link String} token that marks the beginning of the
	 *            template parameter.
	 * 
	 * @param endToken
	 *            the {@link String} token that marks the ending of the template
	 *            parameter.
	 */
	public TokenMarker(String beginToken, String endToken) {
		this.beginToken = beginToken;
		this.endToken = endToken;
	}

	/**
	 * Returns the token that marks the beginning of the template parameter.
	 * 
	 * @return the begin token.
	 */
	public String getBeginToken() {
		return beginToken;
	}

	/**
	 * Returns the token that marks the ending of the template parameter.
	 * 
	 * @return the end token.
	 */
	public String getEndToken() {
		return endToken;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append(beginToken).append(endToken)
				.toString();
	}

}
