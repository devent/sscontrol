package com.anrisoftware.sscontrol.firewall.statements;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Set the firewall in allow all per default.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
public class AllowDefault implements Serializable {

	@Override
	public String toString() {
		return new ToStringBuilder(this).toString();
	}
}
