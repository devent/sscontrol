package com.anrisoftware.sscontrol.firewall.statements;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.inject.Inject;

/**
 * Set the firewall in deny all per default.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class DenyDefault implements Serializable {

	/**
	 * @version 1.0
	 */
	private static final long serialVersionUID = -2958699597541048673L;

	@Inject
	DenyDefault() {
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).toString();
	}
}
