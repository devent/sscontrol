/*
 * Copyright 2012-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-database.
 *
 * sscontrol-database is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-database is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-database. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.database.service;

import java.util.concurrent.ExecutorService;

import org.mangosdk.spi.ProviderFor;

import com.anrisoftware.sscontrol.core.api.ProfileService;
import com.anrisoftware.sscontrol.core.api.Service;
import com.anrisoftware.sscontrol.core.api.ServiceFactory;
import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * Creates the database service.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@ProviderFor(ServiceFactory.class)
public class DatabaseServiceFactory implements ServiceFactory {

	/**
	 * The name of the database service.
	 */
	public static final String NAME = "database";

	private static final Module[] MODULES = new Module[] { new DatabaseModule() };

	private Injector injector;

    private ExecutorService threads;

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public Service create(ProfileService profile) {
		DatabaseServiceImpl service;
		service = injector.getInstance(DatabaseServiceImpl.class);
		service.setProfile(profile);
        service.setThreads(threads);
		return service;
	}

	@Override
	public void setParent(Object parent) {
		this.injector = ((Injector) parent).createChildInjector(MODULES);
	}

    @Override
    public void setThreads(ExecutorService threads) {
        this.threads = threads;
    }
}
