/*
 * Copyright 2012-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd.
 *
 * sscontrol-httpd is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.service

httpd {
    // reference service with id "idapache2"
    refservice "idapache2"
    // http
    bind local, port: 8080
    // https
    bind local, port: 8082
    // domain "test1.com"
	domain "test1.com", address: "192.168.0.50", port: 8080, {
	}
    // domain "test2.com"
	domain "test2.com", address: "192.168.0.50", port: 8082, {
	}
    // domain "test3.com"
    domain "test3.com", address: "192.168.0.50", port: 8082, {
    }
    // domain "test4.com"
    domain "test4.com", address: "192.168.0.50", port: 8082, {
    }
}
