/*
 * Copyright 2013-2014 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.httpd.apache.roundcube.ubuntu_10_04;

import static com.anrisoftware.sscontrol.httpd.apache.apache.ubuntu_10_04.Ubuntu_10_04_ScriptFactory.PROFILE;
import static com.google.inject.multibindings.MapBinder.newMapBinder;
import static java.lang.String.format;

import com.anrisoftware.sscontrol.httpd.apache.roundcube.api.RoundcubeDatabaseConfig;
import com.anrisoftware.sscontrol.httpd.apache.roundcube.linux.RoundcubeMysqlConfig;
import com.anrisoftware.sscontrol.httpd.webservice.ServiceConfig;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.MapBinder;

/**
 * Binds the <i>Roundcube</i> <i>Ubuntu 10.04.</i>
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class Ubuntu_10_04_RoundcubeModule extends AbstractModule {

	@Override
	protected void configure() {
		bindServiceConfig();
        bindBackendConfig();
	}

	private void bindServiceConfig() {
		MapBinder<String, ServiceConfig> map = newMapBinder(binder(),
				String.class, ServiceConfig.class);
		map.addBinding(format("%s.%s", PROFILE, UbuntuConfig.NAME)).to(
				UbuntuConfig.class);
	}

    private void bindBackendConfig() {
        MapBinder<String, RoundcubeDatabaseConfig> map = newMapBinder(binder(),
                String.class, RoundcubeDatabaseConfig.class);
        map.addBinding(format("%s.%s", PROFILE, RoundcubeMysqlConfig.NAME)).to(
                RoundcubeMysqlConfig.class);
    }
}
