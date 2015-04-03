/*
 * Copyright 2013-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-nginx.
 *
 * sscontrol-httpd-nginx is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-nginx is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-nginx. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.nginx.authfile.ubuntu_14_04;

import static com.google.inject.multibindings.MapBinder.newMapBinder;
import static java.lang.String.format;

import com.anrisoftware.sscontrol.httpd.webservice.ServiceConfig;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.MapBinder;

/**
 * <i>Auth/file Ubuntu 14.04</i> configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class Ubuntu_14_04_AuthFileModule extends AbstractModule {

    /**
     * Name of the profile.
     */
    public static final String PROFILE = "ubuntu_14_04";

    /**
     * Authentication service name.
     */
    public static final String SERVICE_NAME = "auth-file";

    @Override
    protected void configure() {
        bindServiceConfig();
    }

    private void bindServiceConfig() {
        MapBinder<String, ServiceConfig> map = newMapBinder(binder(),
                String.class, ServiceConfig.class);
        map.addBinding(format("%s.%s", PROFILE, SERVICE_NAME)).to(
                UbuntuConfig.class);
    }
}
