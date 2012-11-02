/*
 * Copyright 2012 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of noisedriver-crbsnoise. All rights reserved.
 */
package com.anrisoftware.sscontrol.template.service;

import org.apache.commons.lang3.concurrent.ConcurrentException;
import org.mangosdk.spi.ProviderFor;

import com.anrisoftware.sscontrol.template.api.TemplateFactory;
import com.anrisoftware.sscontrol.template.api.TemplateService;
import com.anrisoftware.sscontrol.template.api.TemplateServiceInfo;
import com.google.inject.Injector;

/**
 * Provides the template that is using a <a
 * href=http://www.antlr.org/wiki/display/ST4/StringTemplate+4+Wiki+Home>String
 * Template</a> as the template engine as a service.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 0.1
 */
@ProviderFor(TemplateService.class)
public class StTemplateService implements TemplateService {

	public static final String NAME = "st_template";

	private final LazyInjector lazyInjector;

	public StTemplateService() {
		this.lazyInjector = new LazyInjector();
	}

	@Override
	public void setParent(Object parent) {
		lazyInjector.setParentInjector((Injector) parent);
	}

	@Override
	public TemplateServiceInfo getInfo() {
		return new TemplateServiceInfo(NAME);
	}

	@Override
	public TemplateFactory getTemplate() {
		return getInjector().getInstance(TemplateFactory.class);
	}

	private Injector getInjector() {
		try {
			return lazyInjector.get();
		} catch (ConcurrentException e) {
			throw new RuntimeException(e);
		}
	}

}
