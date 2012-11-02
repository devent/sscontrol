/*
 * Copyright 2012 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of noisedriver-crbsnoise. All rights reserved.
 */
package com.anrisoftware.sscontrol.template.st;

import static com.google.inject.Guice.createInjector;

import org.apache.commons.lang3.concurrent.ConcurrentException;
import org.apache.commons.lang3.concurrent.LazyInitializer;

import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * Lazy create the injector with the needed modules for the template service.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
class LazyInjector extends LazyInitializer<Injector> {

	private Injector parentInjector;

	/**
	 * Sets the optional parent injector.
	 * 
	 * @param injector
	 *            the parent {@link Injector}.
	 */
	public void setParentInjector(Injector injector) {
		parentInjector = injector;
	}

	@Override
	protected Injector initialize() throws ConcurrentException {
		Module[] modules = new Module[] { new StTemplateModule() };
		if (parentInjector != null) {
			return parentInjector.createChildInjector(modules);
		}
		return createInjector(modules);
	}

}
