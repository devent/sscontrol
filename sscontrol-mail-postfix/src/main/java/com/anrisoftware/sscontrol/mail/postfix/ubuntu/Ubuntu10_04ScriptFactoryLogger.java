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

	private static final String ERROR_CREATE_SCRIPT_MESSAGE = "Error create {}-{} script.";
	private static final String SCRIPT_FACTORY = "script factory";
	private static final String ERROR_CREATE_SCRIPT = "Error create script";

	/**
	 * Create logger for {@link Ubuntu10_04ScriptFactory}.
	 */
	Ubuntu10_04ScriptFactoryLogger() {
		super(Ubuntu10_04ScriptFactory.class);
	}

	ServiceException errorCreateScript(Ubuntu10_04ScriptFactory factory,
			Throwable cause) {
		return logException(
				new ServiceException(ERROR_CREATE_SCRIPT, cause).addContextValue(
						SCRIPT_FACTORY, factory), ERROR_CREATE_SCRIPT_MESSAGE,
				factory.getInfo().getServiceName(), factory.getInfo()
						.getProfileName());
	}
}
