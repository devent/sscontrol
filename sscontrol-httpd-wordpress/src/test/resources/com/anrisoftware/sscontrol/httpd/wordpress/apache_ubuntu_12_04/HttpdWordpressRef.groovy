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
package com.anrisoftware.sscontrol.httpd.wordpress.apache_ubuntu_12_04

def wordpressid = "wordpress3"

httpd {
    domain "www.test1.com", address: "192.168.0.51", {
        setup "wordpress_4", id: wordpressid, alias: "wordpress3", {
            database "wordpressdb", user: "user", password: "userpass", host: "localhost"
        }
    }
    ssl_domain "www.test1.com", address: "192.168.0.51", {
        certificate file: Ubuntu_12_04_Resources.certCrt.resource, key: Ubuntu_12_04_Resources.certKey.resource
        setup "wordpress_4", ref: wordpressid
    }
}
