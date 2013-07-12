package com.anrisoftware.sscontrol.dns.maradns.ubuntu;

import org.apache.commons.lang3.concurrent.ConcurrentException;
import org.mangosdk.spi.ProviderFor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.core.api.ServiceScriptFactory;
import com.anrisoftware.sscontrol.core.api.ServiceScriptInfo;
import com.google.inject.Injector;

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
