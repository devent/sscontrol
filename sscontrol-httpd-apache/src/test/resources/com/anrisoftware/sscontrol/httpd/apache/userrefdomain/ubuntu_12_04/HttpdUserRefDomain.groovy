/*
 * Copyright 2013-2014 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.httpd.apache.userrefdomain.ubuntu_12_04

import com.anrisoftware.sscontrol.httpd.apache.ubuntu.UbuntuResources

def domain3id = "test3"

httpd {
    domain "test1.com", address: "192.168.0.50", {
        // web_001
    }
    ssl_domain "test1.com", address: "192.168.0.50", {
        // web_001
        certification_file UbuntuResources.certCrt.resource
        certification_key_file UbuntuResources.certKey.resource
    }
    domain "www.test1.com", address: "192.168.0.50", {
        // web_002
    }
    ssl_domain "test2.com", address: "192.168.0.50", {
        // web_003
        certification_file UbuntuResources.certCrt.resource
        certification_key_file UbuntuResources.certKey.resource
    }
    domain "test3.com", id: domain3id, address: "192.168.0.50", {
        // web_004
    }
    domain "www.test3.com", address: "192.168.0.50", {
        // web_004
        user refdomain: domain3id  //.
    }
    domain "test4.com", address: "192.168.0.50", {
        // web_004
        user refdomain: domain3id //.
    }
    domain "www.test4.com", address: "192.168.0.50", {
        // web_004
        user refdomain: domain3id //.
    }
}
