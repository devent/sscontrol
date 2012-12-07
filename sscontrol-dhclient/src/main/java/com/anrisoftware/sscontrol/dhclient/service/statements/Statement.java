package com.anrisoftware.sscontrol.dhclient.service.statements;

import java.io.Serializable;

import javax.inject.Inject;

/**
 * Dhclient statement.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
abstract class Statement implements Serializable {

	/**
	 * @version 1.0
	 */
	private static final long serialVersionUID = 5678903096130566242L;

	protected final StatementLogger log;

	@Inject
	Statement(StatementLogger logger) {
		this.log = logger;
	}

}
