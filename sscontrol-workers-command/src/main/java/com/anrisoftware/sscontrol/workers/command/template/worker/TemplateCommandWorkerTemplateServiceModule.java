package com.anrisoftware.sscontrol.workers.command.template.worker;

import static java.util.ServiceLoader.load;

import javax.inject.Singleton;

import com.anrisoftware.sscontrol.template.api.TemplateFactory;
import com.anrisoftware.sscontrol.template.api.TemplateService;
import com.anrisoftware.sscontrol.template.api.TemplateServiceInfo;
import com.anrisoftware.sscontrol.workers.api.WorkerException;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

/**
 * Provides the template service for the template command worker.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
public class TemplateCommandWorkerTemplateServiceModule extends AbstractModule {

	private final TemplateServiceInfo info;

	/**
	 * Sets the service information for the requested template service.
	 * 
	 * @param info
	 *            the {@link TemplateServiceInfo}.
	 */
	public TemplateCommandWorkerTemplateServiceModule(TemplateServiceInfo info) {
		this.info = info;
	}

	@Override
	protected void configure() {
	}

	@Provides
	@Singleton
	TemplateFactory getTemplateFactory() throws WorkerException {
		TemplateService service = getTemplateService();
		return service.getTemplate();
	}

	private TemplateService getTemplateService() throws WorkerException {
		for (TemplateService service : load(TemplateService.class)) {
			if (service.getInfo().equals(info)) {
				return service;
			}
		}
		throw new WorkerException("Could not find the template service")
				.addContextValue("information", info);
	}
}
