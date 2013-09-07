/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
 * 
 * This file is part of sscontrol-httpd.
 * 
 * sscontrol-httpd is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * sscontrol-httpd is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.ldap.service;

import org.mangosdk.spi.ProviderFor;

import com.anrisoftware.sscontrol.core.api.ProfileService;
import com.anrisoftware.sscontrol.core.api.Service;
import com.anrisoftware.sscontrol.core.api.ServiceFactory;
import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * Creates the LDAP service.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@ProviderFor(ServiceFactory.class)
public class LdapFactory implements ServiceFactory {

	/**
	 * The name of the LDAP service.
	 */
	public static final String NAME = "ldap";

	private static final Module[] MODULES = new Module[] { new LdapModule() };

	private Injector injector;

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public Service create(ProfileService profile) {
		LdapServiceImpl service;
		service = injector.getInstance(LdapServiceImpl.class);
		service.setProfile(profile);
		return service;
	}

	@Override
	public void setParent(Object parent) {
		this.injector = ((Injector) parent).createChildInjector(MODULES);
	}
}
