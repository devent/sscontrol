package com.anrisoftware.sscontrol.httpd.statements;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Redirect statement.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class Redirect {

	@Override
	public String toString() {
		return new ToStringBuilder(this).toString();
	}
}
