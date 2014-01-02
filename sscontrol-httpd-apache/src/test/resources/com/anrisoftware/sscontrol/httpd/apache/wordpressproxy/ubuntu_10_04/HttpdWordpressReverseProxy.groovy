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
package com.anrisoftware.sscontrol.httpd.apache.wordpressproxy.ubuntu_10_04

import com.anrisoftware.sscontrol.httpd.apache.ubuntu.UbuntuResources;

httpd {
    // reference service with id "idproxy"
    refservice "idproxy"
    // domain test1.com
    domain "test1.com", address: "192.168.0.50", {
        user "web_001", uid: 2001, group: "web_001", gid: 2001
        redirect to_www
    }
    // SSL/domain test1.com
    ssl_domain "test1.com", address: "192.168.0.50", {
        user "web_001", uid: 2001, group: "web_001", gid: 2001
        redirect to_www
        certification_file UbuntuResources.certCrt.resource
        certification_key_file UbuntuResources.certKey.resource
    }
    // domain www.test1.com
    domain "www.test1.com", address: "192.168.0.51", {
        user "web_002", uid: 2002, group: "web_002", gid: 2002
        setup "proxy", service: "wordpress", alias: "wordpress3", address: "http://127.0.0.1:8080" //.
    }
    // SSL/domain www.test1.com
    ssl_domain "www.test1.com", address: "192.168.0.51", {
        user "web_002", uid: 2002, group: "web_002", gid: 2002
        setup "proxy", service: "wordpress", alias: "wordpress3", address: "https://127.0.0.1:8082" //.
        certification_file UbuntuResources.certCrt.resource
        certification_key_file UbuntuResources.certKey.resource
    }
}
