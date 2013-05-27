package com.anrisoftware.sscontrol.mail.postfix.ubuntu;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.mangosdk.spi.ProviderFor;

import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.core.api.ServiceScriptFactory;
import com.anrisoftware.sscontrol.core.api.ServiceScriptInfo;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * Provides the postfix script for Ubuntu 10.04 server.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@ProviderFor(ServiceScriptFactory.class)
public class Ubuntu10_04ScriptFactory implements ServiceScriptFactory {

	/**
	 * Name of the service.
	 */
	public static final String NAME = "postfix";

	/**
	 * Name of the profile.
	 */
	public static final String PROFILE_NAME = "ubuntu_10_04";

	/**
	 * {@link ServiceScriptInfo} information identifying this service.
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

	private static Ubuntu10_04ScriptFactoryLogger log = new Ubuntu10_04ScriptFactoryLogger();

	private static final Module[] MODULES = new Module[] { new UbuntuModule() };

	private Injector parent;

	@Override
	public ServiceScriptInfo getInfo() {
		return INFO;
	}

	@Override
	public Object getScript() throws ServiceException {
		try {
			return createInjector(parent).getInstance(Ubuntu_10_04Script.class);
		} catch (Exception e) {
			throw log.errorCreateScript(this, e);
		}
	}

	private Injector createInjector(Injector parent) {
		return parent == null ? Guice.createInjector(MODULES) : parent
				.createChildInjector(MODULES);
	}

	@Override
	public void setParent(Object parent) {
		this.parent = (Injector) parent;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append(INFO).toString();
	}
}
