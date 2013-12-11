/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-apache.
 *
 * sscontrol-httpd-apache is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-apache is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-apache. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.apache.linux.roundcube;

import static com.google.inject.multibindings.MapBinder.newMapBinder;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.MapBinder;

/**
 * Roundcube web service module.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class RoundcubeModule extends AbstractModule {

    @Override
    protected void configure() {
        bindDatabaseConfig();
    }

    private void bindDatabaseConfig() {
        MapBinder<String, RoundcubeDatabaseConfig> map = newMapBinder(binder(),
                String.class, RoundcubeDatabaseConfig.class);
        map.addBinding(RoundcubeMysqlConfig.NAME)
                .to(RoundcubeMysqlConfig.class);
    }
}
