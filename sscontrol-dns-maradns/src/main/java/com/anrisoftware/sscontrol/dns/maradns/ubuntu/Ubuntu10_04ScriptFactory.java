package com.anrisoftware.sscontrol.dns.maradns.ubuntu;

import org.mangosdk.spi.ProviderFor;

import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.core.api.ServiceScriptFactory;
import com.anrisoftware.sscontrol.core.api.ServiceScriptInfo;
import com.google.inject.Injector;
import com.google.inject.Module;

@ProviderFor(ServiceScriptFactory.class)
public class Ubuntu10_04ScriptFactory implements ServiceScriptFactory {

	public static final String NAME = "maradns";

	public static final String PROFILE_NAME = "ubuntu_10_04";

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
