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
package com.anrisoftware.sscontrol.httpd.apache.wordpressproxy.ubuntu_12_04

import com.anrisoftware.sscontrol.httpd.apache.wordpress.ubuntu_12_04.Ubuntu_12_04_Resources

def http = 8080
def https = 8082

httpd {
    // reference service with id "idapache2"
    refservice "idapache2"
    // http
    bind "192.168.0.51", ports: [http, https]
    // https
    bind local, port: 8082
    // domain www.test1.com
    domain "www.test1.com", address: "192.168.0.51", port: http, {
        user "web_002", uid: 2002, group: "web_002", gid: 2002
        setup "wordpress_4", id: "test1wordpress", alias: "wordpress4", {
            database "wordpress4", user: "user", password: "userpass", host: "localhost"
        }
    }
    // SSL/domain www.test1.com
    ssl_domain "www.test1.com", address: "192.168.0.51", port: https, {
        user "web_002", uid: 2002, group: "web_002", gid: 2002
        certificate file: Ubuntu_12_04_Resources.certCrt.resource, key: Ubuntu_12_04_Resources.certKey.resource
        setup "wordpress_4", ref: "test1wordpress"
    }
}
