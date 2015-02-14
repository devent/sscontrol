/*
 * Copyright 2012-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-dns.
 *
 * sscontrol-dns is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-dns is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-dns. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.dns.service;

import java.util.concurrent.ExecutorService;

import org.mangosdk.spi.ProviderFor;

import com.anrisoftware.sscontrol.core.api.ProfileService;
import com.anrisoftware.sscontrol.core.api.Service;
import com.anrisoftware.sscontrol.core.api.ServiceFactory;
import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * Creates the DNS service.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@ProviderFor(ServiceFactory.class)
public class DnsServiceFactory implements ServiceFactory {

    /**
     * The name of the DNS service.
     */
    public static final String SERVICE_NAME = "dns";

    private static final Module[] MODULES = new Module[] { new DnsModule() };

    private Injector injector;

    private ExecutorService threads;

    @Override
    public String getName() {
        return SERVICE_NAME;
    }

    @Override
    public Service create(ProfileService profile) {
        DnsServiceImpl service;
        service = injector.getInstance(DnsServiceImpl.class);
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
