/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-repo.
 *
 * sscontrol-repo is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-repo is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-repo. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.repo.service;

import java.util.concurrent.ExecutorService;

import org.mangosdk.spi.ProviderFor;

import com.anrisoftware.sscontrol.core.api.ProfileService;
import com.anrisoftware.sscontrol.core.api.Service;
import com.anrisoftware.sscontrol.core.api.ServiceFactory;
import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * Creates the repository service and makes it available as a service.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@ProviderFor(ServiceFactory.class)
public class RepoServiceFactory implements ServiceFactory {

    /**
     * The name of the repository service.
     */
    public static final String NAME = "repo";

    private static final Module[] MODULES = new Module[] { new RepoModule() };

    private Injector injector;

    private ExecutorService threads;

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Service create(ProfileService profile) {
        RepoServiceImpl service;
        service = injector.getInstance(RepoServiceImpl.class);
        service.setThreads(threads);
        service.setProfile(profile);
        return service;
    }

    @Override
    public void setParent(Object parent) {
        this.injector = ((Injector) parent).createChildInjector(MODULES);
    }

    /**
     * Sets the threads pool.
     *
     * @param threads
     *            the {@link ExecutorService} threads pool.
     */
    @Override
    public void setThreads(ExecutorService threads) {
        this.threads = threads;
    }
}
