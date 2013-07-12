package com.anrisoftware.sscontrol.database.mysql.ubuntu;

import org.apache.commons.lang3.concurrent.ConcurrentException;
import org.mangosdk.spi.ProviderFor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.core.api.ServiceScriptFactory;
import com.anrisoftware.sscontrol.core.api.ServiceScriptInfo;
import com.google.inject.Injector;

/**
 * Provides the database MySQL Ubuntu 10.04 script as a service.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@ProviderFor(ServiceScriptFactory.class)
public class Ubuntu10_04ScriptFactory implements ServiceScriptFactory {

	/**
	 * The name of the MySQL script service.
	 */
	public static final String NAME = "mysql";

	/**
	 * The profile name of the service.
	 */
	public static final String PROFILE_NAME = "ubuntu_10_04";

	/**
	 * The service information of the MySQL Ubuntu 10.04 script service.
	 */
	public static ServiceScriptInfo INFO = new ServiceScriptInfo() {

		@Override
		public String getServiceName() {
			return NAME;
		}

		@Override
		public String getProfileName() {
			return PROFILE_NAME;
		}
	};

	private static Logger log = LoggerFactory
			.getLogger(Ubuntu10_04ScriptFactory.class);

	private static LazyInjector lazyInjector = new LazyInjector();

	@Override
	public ServiceScriptInfo getInfo() {
		return INFO;
	}

	@Override
	public Object getScript() throws ServiceException {
		try {
			return lazyInjector.get().getInstance(Ubuntu_10_04Script.class);
		} catch (ConcurrentException e) {
			throw errorCreateScript(e.getCause());
		}
	}

	private ServiceException errorCreateScript(Throwable cause) {
		ServiceException ex = new ServiceException("Error create script", cause);
		ex.add("service name", NAME);
		ex.add("profile name", PROFILE_NAME);
		log.error(ex.getLocalizedMessage());
		return ex;
	}

	@Override
	public void setParent(Object parent) {
		lazyInjector.setParentInjector((Injector) parent);
	}

}
