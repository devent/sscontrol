/*
 * Copyright 2012 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-hostname.
 *
 * sscontrol-hostname is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-hostname is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-hostname. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.mail.service;

import java.util.ServiceLoader;

import javax.inject.Singleton;

import com.anrisoftware.sscontrol.core.api.ServiceScriptFactory;
import com.anrisoftware.sscontrol.mail.statements.StatementsModule;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

/**
 * Binds the mail service.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class MailModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new StatementsModule());
	}

	@Provides
	@Singleton
	ServiceLoader<ServiceScriptFactory> getDnsServiceScripts() {
		return ServiceLoader.load(ServiceScriptFactory.class);
	}
}
