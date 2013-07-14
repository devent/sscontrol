package com.anrisoftware.sscontrol.database.mysql.ubuntu;

import org.mangosdk.spi.ProviderFor;

import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.core.api.ServiceScriptFactory;
import com.anrisoftware.sscontrol.core.api.ServiceScriptInfo;
import com.google.inject.Injector;
import com.google.inject.Module;

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

	private static final Module[] MODULES = new Module[] { new UbuntuModule() };

	private Injector injector;

	@Override
	public ServiceScriptInfo getInfo() {
		return INFO;
	}

	@Override
	public Object getScript() throws ServiceException {
		return injector.getInstance(Ubuntu_10_04Script.class);
	}

	@Override
	public void setParent(Object parent) {
		this.injector = ((Injector) parent).createChildInjector(MODULES);
	}

}
