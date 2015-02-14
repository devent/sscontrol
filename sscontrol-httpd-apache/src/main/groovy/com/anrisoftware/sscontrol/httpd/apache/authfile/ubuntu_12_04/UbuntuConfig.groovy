/*
 * Copyright 2013-2015 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.httpd.apache.authfile.ubuntu_12_04

import static com.anrisoftware.sscontrol.httpd.apache.apache.ubuntu_12_04.Ubuntu_12_04_ScriptFactory.PROFILE

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.httpd.apache.authfile.apache_2_2.AuthFileConfig
import com.anrisoftware.sscontrol.httpd.webservice.ServiceConfig

/**
 * <i>Auth-file Ubuntu 12.04</i> configuration.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class UbuntuConfig extends AuthFileConfig implements ServiceConfig {

    @Inject
    UbuntuPropertiesProvider authProperties

    @Override
    ContextProperties getAuthProperties() {
        authProperties.get()
    }

    @Override
    String getProfile() {
        PROFILE
    }
}
