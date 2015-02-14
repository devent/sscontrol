/*
 * Copyright 2013-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-security.
 *
 * sscontrol-security is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-security is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-security. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.security.service;

import static com.google.inject.multibindings.MapBinder.newMapBinder;

import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.MapBinder;

/**
 * Binds the security service.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class SecurityModule extends AbstractModule {

    private static final String SERVICE_NAME = "service name";
    private static final String EMPTY_NAME = "0";

    @Override
    protected void configure() {
        bindEmptyService();
    }

    private void bindEmptyService() {
        MapBinder<String, SecServiceFactory> mapbinder;
        mapbinder = newMapBinder(binder(), String.class,
                SecServiceFactory.class);
        mapbinder.addBinding(EMPTY_NAME).toInstance(new SecServiceFactory() {

            @Override
            public SecService create(Map<String, Object> args) {
                return new SecService() {

                    @Override
                    public String getName() {
                        return EMPTY_NAME;
                    }

                    @Override
                    public String toString() {
                        return new ToStringBuilder(this).append(SERVICE_NAME,
                                getName()).toString();
                    }
                };
            }
        });
    }
}
