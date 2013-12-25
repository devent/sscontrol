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
package com.anrisoftware.sscontrol.httpd.nginx.ubuntu_10_04.proxywordpress

import static org.apache.commons.io.FileUtils.*

import org.apache.commons.lang3.StringUtils

import com.anrisoftware.sscontrol.httpd.nginx.linux.proxywordpress.BaseProxyWordpressConfig
import com.anrisoftware.sscontrol.httpd.nginx.ubuntu_10_04.nginx.Ubuntu10_04ScriptFactory
import com.anrisoftware.sscontrol.httpd.statements.proxy.ProxyService

/**
 * Configures proxy service for the Wordpress service for Ubuntu 10.04.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class ProxyWordpressConfig extends BaseProxyWordpressConfig {

    /**
     * Name of the proxy service.
     */
    public static final String SERVICE_NAME = "wordpress"

    /**
     * Returns the proxy cache name,
     * for example {@code "wordpressstaticfilecache".}
     *
     * <ul>
     * <li>profile property {@code "wordpress_proxy_cache_name"}</li>
     * </ul>
     *
     * @see #getDefaultProperties()
     */
    String proxyCacheName(ProxyService service) {
        def str = profileProperty "wordpress_proxy_cache_name", defaultProperties
        def name = StringUtils.replace service.domain.name, ".", "_"
        String.format str, name
    }

    @Override
    String getProxyService() {
        SERVICE_NAME
    }

    @Override
    String getProfile() {
        Ubuntu10_04ScriptFactory.PROFILE
    }
}
