/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.httpd.apache.wordpress.ubuntu_12_04

def domainid = "wwwtest1"
def wordpressid = "wordpress3"
def userid = "web_001"
def groupid = "web_001"

httpd {
    domain "www.test1.com", id: domainid, address: "192.168.0.51", {
        user userid, group: groupid
        setup "wordpress", id: wordpressid, alias: "/", {
            database "wordpress3", user: "user", password: "userpass", host: "localhost"
            multisite setup: "subdir"
        }
    }
    domain "www.blogfoo.com", address: "192.168.0.51", {
        user userid, group: groupid
        setup "wordpress", ref: wordpressid, refdomain: domainid
    }
    domain "www.blogbar.com", address: "192.168.0.51", {
        user userid, group: groupid
        setup "wordpress", ref: wordpressid, refdomain: domainid
    }
}
