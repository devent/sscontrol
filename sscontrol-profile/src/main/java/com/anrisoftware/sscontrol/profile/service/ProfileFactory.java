/*
 * Copyright 2012-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-profile.
 *
 * sscontrol-profile is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-profile is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-profile. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.profile.service;

import java.util.concurrent.ExecutorService;

import org.apache.commons.lang3.concurrent.ConcurrentException;
import org.mangosdk.spi.ProviderFor;

import com.anrisoftware.sscontrol.core.api.ProfileService;
import com.anrisoftware.sscontrol.core.api.Service;
import com.anrisoftware.sscontrol.core.api.ServiceFactory;
import com.google.inject.Injector;

/**
 * Provides the profile service.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@ProviderFor(ServiceFactory.class)
public class ProfileFactory implements ServiceFactory {

    public static final String NAME = "profile";

    private final LazyInjector lazyInjector;

    public ProfileFactory() {
        this.lazyInjector = new LazyInjector();
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Service create(ProfileService profile) {
        try {
            return lazyInjector.get().getInstance(Service.class);
        } catch (ConcurrentException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setParent(Object injector) {
        lazyInjector.setParentInjector((Injector) injector);
    }

    @Override
    public void setThreads(ExecutorService threads) {
    }

}
