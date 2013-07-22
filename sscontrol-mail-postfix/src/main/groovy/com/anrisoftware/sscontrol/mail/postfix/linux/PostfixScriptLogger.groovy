package com.anrisoftware.sscontrol.mail.postfix.linux

import com.anrisoftware.globalpom.log.AbstractLogger

/**
 * Logging messages for {@link PostfixScriptLogger}.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class PostfixScriptLogger extends AbstractLogger {

	/**
	 * Create logger for {@link PostfixScriptLogger}.
	 */
	PostfixScriptLogger() {
		super(PostfixScriptLogger.class)
	}
}
