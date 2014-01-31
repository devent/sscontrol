/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.httpd.nginx.generalproxy.ubuntu_10_04

import static org.apache.commons.io.FileUtils.*

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.httpd.nginx.generalproxy.linux.GeneralProxyConfig
import com.anrisoftware.sscontrol.httpd.nginx.nginx.ubuntu_10_04.Ubuntu_10_04_ScriptFactory
import com.anrisoftware.sscontrol.httpd.proxy.ProxyConfig;

/**
 * Ubuntu 10.04 General Proxy.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class UbuntuConfig extends GeneralProxyConfig implements ProxyConfig {

    @Inject
    private UbuntuPropertiesProvider ubuntuPropertiesProvider

    @Override
    ContextProperties getProxyProperties() {
        ubuntuPropertiesProvider.get()
    }

    @Override
    String getProfile() {
        Ubuntu_10_04_ScriptFactory.PROFILE
    }
}
