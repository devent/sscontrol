/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.httpd.apache.wordpress.ubuntu_10_04

import com.anrisoftware.sscontrol.httpd.apache.core.ubuntu_10_04.UbuntuResources

httpd {
    // http
    bind port: 8080
    // https
    bind port: 8082
    // domain test1.com
    domain "test1.com", address: "192.168.0.50", port: 8080, { //.
        redirect to_www //.
    }
    // SSL/domain test1.com
    ssl_domain "test1.com", address: "192.168.0.50", port: 8082, {
        certification_file UbuntuResources.certCrt.resource
        certification_key_file UbuntuResources.certKey.resource
        redirect to_www
    }
    // domain www.test1.com
    domain "www.test1.com", address: "192.168.0.51", port: 8080, {
        proxy reverse_cache
        setup "wordpress", alias: "wordpress3", {
            database "wordpress3", user: "user", password: "userpass", host: "localhost"
        }
    }
    // SSL/domain www.test1.com
    ssl_domain "www.test1.com", address: "192.168.0.51", port: 8082, {
        certification_file UbuntuResources.certCrt.resource
        certification_key_file UbuntuResources.certKey.resource
    }
}
