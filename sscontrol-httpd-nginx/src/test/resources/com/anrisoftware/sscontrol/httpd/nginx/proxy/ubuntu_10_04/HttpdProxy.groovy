/*
 * Copyright 2013-2014 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.httpd.nginx.proxy.ubuntu_10_04

httpd {
    // domain test1.com
    domain "test1.com", address: "192.168.0.50", {
        redirect to: "www.%"
    }
    // SSL/domain test1.com
    ssl_domain "test1.com", address: "192.168.0.50", {
        redirect to: "www.%"
        certification_file ProxyResources.certCrt.resource
        certification_key_file ProxyResources.certKey.resource
    }
    // domain www.test1.com
    domain "www.test1.com", address: "192.168.0.51", {
        setup "proxy", service: "general", alias: "sitefoo", proxyname: "sitefoo", address: "http://127.0.0.1:8080"
    }
    // SSL/domain www.test1.com
    ssl_domain "www.test1.com", address: "192.168.0.51", {
        setup "proxy", service: "general", alias: "sitefoo", proxyname: "sitefoo", address: "https://127.0.0.1:8082"
        certification_file ProxyResources.certCrt.resource
        certification_key_file ProxyResources.certKey.resource
    }
}
