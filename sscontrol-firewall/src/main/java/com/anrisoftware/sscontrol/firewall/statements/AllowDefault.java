package com.anrisoftware.sscontrol.firewall.statements;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.inject.Inject;

/**
 * Set the firewall in allow all per default.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class AllowDefault implements Serializable {

	/**
	 * @version 1.0
	 */
	private static final long serialVersionUID = -9087937429345491627L;

	@Inject
	AllowDefault() {
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).toString();
	}
}
