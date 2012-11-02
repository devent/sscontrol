/*
 * Copyright 2012 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-workers-command.
 *
 * sscontrol-workers-command is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-workers-command is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-workers-command. If not, see <http://www.gnu.org/licenses/>.
 */
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
