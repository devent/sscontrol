/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-wordpress.
 *
 * sscontrol-httpd-wordpress is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-wordpress is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-wordpress. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.wordpress.nginx.proxy.ubuntu_12_04

import static org.apache.commons.io.FileUtils.*

import javax.inject.Inject

import com.anrisoftware.propertiesutils.ContextProperties
import com.anrisoftware.sscontrol.httpd.nginx.nginx.ubuntu_12_04.Ubuntu_12_04_ScriptFactory
import com.anrisoftware.sscontrol.httpd.proxy.ProxyConfig
import com.anrisoftware.sscontrol.httpd.wordpress.nginx.proxy.linux.WordpressNginxProxyConfig

/**
 * <i>Ubuntu 12.04 Nginx Wordpress</i> proxy.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class UbuntuNginxWordpressProxyConfig extends WordpressNginxProxyConfig implements ProxyConfig {

    @Inject
    private UbuntuNginxWordpressProxyPropertiesProvider ubuntuPropertiesProvider

    @Override
    ContextProperties getProxyProperties() {
        ubuntuPropertiesProvider.get()
    }

    @Override
    String getProfile() {
        Ubuntu_12_04_ScriptFactory.PROFILE
    }
}
