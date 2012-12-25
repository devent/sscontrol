package com.anrisoftware.sscontrol.firewall.statements;

/**
 * Factory to create a new allow default statement.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface AllowDefaultFactory {

	/**
	 * Creates a new allow default statement.
	 * 
	 * @return the {@link AllowDefault}.
	 */
	AllowDefault create();
}
