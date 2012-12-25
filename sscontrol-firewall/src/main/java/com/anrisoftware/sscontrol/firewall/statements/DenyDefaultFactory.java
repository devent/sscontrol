package com.anrisoftware.sscontrol.firewall.statements;

/**
 * Factory to create a new deny default statement.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface DenyDefaultFactory {

	/**
	 * Creates a new deny default statement.
	 * 
	 * @return the {@link DenyDefault}.
	 */
	DenyDefault create();
}
