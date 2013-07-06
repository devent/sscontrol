package com.anrisoftware.sscontrol.mail.statements;

import javax.inject.Inject;

import com.google.inject.assistedinject.Assisted;

/**
 * Catch-all alias.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class Catchall extends Alias {

	/**
	 * @see CatchallFactory#create(Domain, String)
	 */
	@Inject
	Catchall(AliasLogger logger, @Assisted Domain domain,
			@Assisted String destination) {
		super(logger, domain, "", destination);
	}
}