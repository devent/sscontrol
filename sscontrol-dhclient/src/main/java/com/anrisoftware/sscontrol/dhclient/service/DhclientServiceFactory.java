/*
 * Copyright 2012-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-dhclient.
 *
 * sscontrol-dhclient is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-dhclient is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-dhclient. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.dhclient.service;

import java.util.concurrent.ExecutorService;

import org.mangosdk.spi.ProviderFor;

import com.anrisoftware.sscontrol.core.api.ProfileService;
import com.anrisoftware.sscontrol.core.api.Service;
import com.anrisoftware.sscontrol.core.api.ServiceFactory;
import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * <i>Dhclient</i> service provider.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@ProviderFor(ServiceFactory.class)
public class DhclientServiceFactory implements ServiceFactory {

    /**
     * The name of the <i>dhclient</i> service.
     */
    public static final String NAME = "dhclient";

    private static final Module[] MODULES = { new DhclientModule() };

    private Injector injector;

    private ExecutorService threads;

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Service create(ProfileService profile) {
        DhclientServiceImpl service;
        service = injector.getInstance(DhclientServiceImpl.class);
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
