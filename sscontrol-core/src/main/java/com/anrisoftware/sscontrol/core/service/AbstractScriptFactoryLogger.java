package com.anrisoftware.sscontrol.core.service;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.api.ServiceException;

/**
 * Logging messages for {@link AbstractScriptFactory}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class AbstractScriptFactoryLogger extends AbstractLogger {

	private static final String CREATE_SCRIPT2 = "Error create {}-{} script.";
	private static final String SCRIPT_FACTORY = "script factory";
	private static final String CREATE_SCRIPT = "Error create script";

	/**
	 * Create logger for {@link AbstractScriptFactory}.
	 */
	AbstractScriptFactoryLogger() {
		super(AbstractScriptFactory.class);
	}

	ServiceException errorCreateScript(AbstractScriptFactory factory,
			Throwable cause) {
		return logException(new ServiceException(CREATE_SCRIPT, cause).add(
				SCRIPT_FACTORY, factory), CREATE_SCRIPT2, factory.getInfo()
				.getServiceName(), factory.getInfo().getProfileName());
	}
}
