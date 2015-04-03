/*
 * Copyright 2013-2015 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.httpd.apache.authmysql.ubuntu_12_04

httpd {
    domain "test1.com", address: "192.168.0.50", {
        setup "auth-db", id: "test1id", auth: "Private Directory", location: "/private", {
            database "authdb", user: "userdb", password: "userpassdb", host: "localhost", driver: "mysql"
            require valid: RequireValid.user
        }
    }
    domain "test2.com", address: "192.168.0.50", {
        setup "auth-db", ref: "test1id"
    }
}
