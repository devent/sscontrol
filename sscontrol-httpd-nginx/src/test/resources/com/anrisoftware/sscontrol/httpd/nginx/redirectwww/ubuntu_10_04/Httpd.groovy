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
package com.anrisoftware.sscontrol.httpd.nginx.proxy.ubuntu_10_04

import com.anrisoftware.sscontrol.httpd.nginx.core.ubuntu_10_04.UbuntuResources

httpd {
    // domain test1.com
    domain "test1.com", address: "192.168.0.50", {
        redirect to_www
    }
    // SSL/domain test1.com
    ssl_domain "test1.com", address: "192.168.0.50", {
        redirect to_www
        certification_file UbuntuResources.certCrt.resource
        certification_key_file UbuntuResources.certKey.resource
    }
    // domain www.test1.com
    domain "www.test1.com", address: "192.168.0.51", {
    }
    // SSL/domain www.test1.com
    ssl_domain "www.test1.com", address: "192.168.0.51", {
        certification_file UbuntuResources.certCrt.resource
        certification_key_file UbuntuResources.certKey.resource
    }
}
