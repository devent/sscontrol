/*
 * Copyright 2012 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of noisedriver-crbsnoise. All rights reserved.
 */
package com.anrisoftware.sscontrol.workers.command.exec.service;

import org.apache.commons.lang3.concurrent.ConcurrentException;
import org.mangosdk.spi.ProviderFor;

import com.anrisoftware.sscontrol.workers.api.WorkerFactory;
import com.anrisoftware.sscontrol.workers.api.WorkerService;
import com.anrisoftware.sscontrol.workers.api.WorkerServiceInfo;
import com.google.inject.Injector;

/**
 * Provides the worker that execute a shell command as a service.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
@ProviderFor(WorkerService.class)
public class ExecCommandWorkerService implements WorkerService {

	public static final String NAME = "exec_command_worker";

	private final LazyInjector lazyInjector;

	public ExecCommandWorkerService() {
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
