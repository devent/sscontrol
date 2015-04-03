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
package com.anrisoftware.sscontrol.httpd.nginx.core.ubuntu_12_04

httpd {
    domain "test1.com", address: "192.168.0.50", {
        setup "proxy", service: "general", proxyname: "sitefoo", address: "http://127.0.0.1:8080"
        setup "auth-file", id: "test1authid", auth: "Private Directory", {
            password users: DomainsResources.authbasicprivatePasswdFile.resource
        }
    }
    domain "test2.com", address: "192.168.0.50", {
        setup "auth-file", ref: "test1authid"
    }
}
