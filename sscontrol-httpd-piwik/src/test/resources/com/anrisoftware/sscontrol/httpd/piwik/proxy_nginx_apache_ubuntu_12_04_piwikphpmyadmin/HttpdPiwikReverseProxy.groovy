/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-piwik.
 *
 * sscontrol-httpd-piwik is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-piwik is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-piwik. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.piwik.proxy_nginx_apache_ubuntu_12_04

import com.anrisoftware.sscontrol.httpd.piwik.ubuntu_12_04.Ubuntu_12_04_Resources

httpd {
    // reference service with id "idproxy"
    refservice "idproxy"
    // domain test1.com
    domain "test1.com", address: "192.168.0.50", {
    }
    // SSL/domain test1.com
    ssl_domain "test1.com", address: "192.168.0.50", {
        certificate file: Ubuntu_12_04_Resources.certCrt.resource, key: Ubuntu_12_04_Resources.certKey.resource

        // phpmyadmin
        setup "proxy", service: "general", alias: "phpmyadmin", proxyname: "phpmyadmin", address: "https://127.0.0.1:8082", {
            cache staticFiles: true
        }

        // piwik
        setup "proxy", service: "general", alias: "piwik", proxyname: "piwik", address: "https://127.0.0.1:8082", {
            cache staticFiles: true
        }
    }
}
