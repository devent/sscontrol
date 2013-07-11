package com.anrisoftware.sscontrol.dhclient.statements;

import java.io.Serializable;

import javax.inject.Inject;

/**
 * Dhclient statement.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
abstract class Statement implements Serializable {

	protected final StatementLogger log;

	@Inject
	Statement(StatementLogger logger) {
		this.log = logger;
	}

}
