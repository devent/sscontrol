package com.anrisoftware.sscontrol.workers.text.tokentemplate.worker;

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
