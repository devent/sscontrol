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
package com.anrisoftware.sscontrol.httpd.nginx.staticservice.ubuntu_12_04

import static org.apache.commons.io.FileUtils.*

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.httpd.nginx.nginx.ubuntu_12_04.Ubuntu_12_04_ScriptFactory
import com.anrisoftware.sscontrol.httpd.nginx.staticservice.linux.GeneralStaticConfig
import com.anrisoftware.sscontrol.httpd.webservice.ServiceConfig

/**
 * <i>Ubuntu 12.04 Nginx</i> static file configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class UbuntuConfig extends GeneralStaticConfig implements ServiceConfig {

    @Inject
    private UbuntuPropertiesProvider ubuntuPropertiesProvider

    @Override
    ContextProperties getStaticProperties() {
        ubuntuPropertiesProvider.get()
    }

    @Override
    String getProfile() {
        Ubuntu_12_04_ScriptFactory.PROFILE
    }
}
