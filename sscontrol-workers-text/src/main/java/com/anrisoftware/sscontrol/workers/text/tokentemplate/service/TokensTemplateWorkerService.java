/*
 * Copyright 2012 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-workers-text.
 *
 * sscontrol-workers-text is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-workers-text is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-workers-text. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.workers.text.tokentemplate.service;

import org.apache.commons.lang3.concurrent.ConcurrentException;
import org.mangosdk.spi.ProviderFor;

import com.anrisoftware.sscontrol.workers.api.WorkerFactory;
import com.anrisoftware.sscontrol.workers.api.WorkerService;
import com.anrisoftware.sscontrol.workers.api.WorkerServiceInfo;
import com.google.inject.Injector;

/**
 * Provides the tokens template worker as a service.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@ProviderFor(WorkerService.class)
public class TokensTemplateWorkerService implements WorkerService {

	public static final String NAME = "tokens_template_worker";

	private final LazyInjector lazyInjector;

	public TokensTemplateWorkerService() {
		this.lazyInjector = new LazyInjector();
	}

	@Override
	public void setParent(Object parent) {
		lazyInjector.setParentInjector((Injector) parent);
	}

	@Override
	public WorkerServiceInfo getInfo() {
		return new WorkerServiceInfo(NAME);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends WorkerFactory> T getWorker() {
		return (T) getInjector().getInstance(WorkerFactory.class);
	}

	private Injector getInjector() {
		try {
			return lazyInjector.get();
		} catch (ConcurrentException e) {
			throw new RuntimeException(e);
		}
	}

}
