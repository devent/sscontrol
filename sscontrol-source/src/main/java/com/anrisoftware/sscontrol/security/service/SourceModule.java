/*
 * Copyright 2015-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-source.
 *
 * sscontrol-source is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-source is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-source. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.security.service;

import static com.google.inject.multibindings.MapBinder.newMapBinder;

import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.anrisoftware.sscontrol.source.service.SourceServiceFactory;
import com.anrisoftware.sscontrol.source.service.SourceSetupService;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.MapBinder;

/**
 * Binds the source code management service.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class SourceModule extends AbstractModule {

    private static final String SERVICE_NAME = "service name";
    private static final String EMPTY_NAME = "0";

    @Override
    protected void configure() {
        bindEmptyService();
    }

    private void bindEmptyService() {
        MapBinder<String, SourceServiceFactory> mapbinder;
        mapbinder = newMapBinder(binder(), String.class,
                SourceServiceFactory.class);
        mapbinder.addBinding(EMPTY_NAME).toInstance(new SourceServiceFactory() {

            @Override
            public SourceSetupService create(Map<String, Object> args) {
                return new SourceSetupService() {

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
