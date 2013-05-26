package com.anrisoftware.sscontrol.mail.postfix.ubuntu;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.api.ServiceException;

/**
 * Logging messages for {@link Ubuntu10_04ScriptFactory}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Ubuntu10_04ScriptFactoryLogger extends AbstractLogger {

	/**
	 * Create logger for {@link Ubuntu10_04ScriptFactory}.
	 */
	Ubuntu10_04ScriptFactoryLogger() {
		super(Ubuntu10_04ScriptFactory.class);
	}

	ServiceException errorCreateScript(Ubuntu10_04ScriptFactory factory,
			Throwable cause) {
		ServiceException ex = new ServiceException("Error create script", cause);
		ex.addContextValue("script factory", factory);
		log.error(ex.getLocalizedMessage());
		return ex;
	}
}
