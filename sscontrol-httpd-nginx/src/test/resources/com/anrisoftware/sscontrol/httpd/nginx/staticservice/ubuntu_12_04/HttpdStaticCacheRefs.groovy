/*
 * Copyright 2013-2015 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.httpd.nginx.staticservice.ubuntu_12_04

httpd {
    domain "test1.com", address: "192.168.0.50", {

        // static files cache
        setup "static-cache", id: "static-test1.com", alias: "/static", {
            // include WebDAV and Auth configuration
            include refs: "webdav-test1.com, auth-test1.com"
        }

        setup "webdav", id: "webdav-test1.com", alias: "/static"

        setup "auth-file", id: "auth-test1.com", alias: "/static", auth: "Private Directory", {
            password users: StaticResources.privatePasswdFile.resource
            require except: "GET, OPTIONS"
        }
    }
}
