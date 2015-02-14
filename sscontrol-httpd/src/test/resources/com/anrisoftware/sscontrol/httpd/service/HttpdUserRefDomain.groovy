/*
 * Copyright 2012-2015 Erwin Müller <erwin.mueller@deventm.org>
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

def domain1id = "test1"
def domain3id = "test3"

httpd {
	domain "test1.com", id: domain1id, address: "192.168.0.50", {
	}
    ssl_domain "test1.com", address: "192.168.0.50", {
        user refdomain: domain1id
    }
    ssl_domain "test2.com", address: "192.168.0.50", {
    }
    domain "www.test1.com", address: "192.168.0.50", {
    }
    domain "test3.com", id: domain3id, address: "192.168.0.50", {
    }
    domain "www.test3.com", address: "192.168.0.50", {
        user refdomain: domain3id
    }
    domain "test4.com", address: "192.168.0.50", {
        user refdomain: domain3id
    }
    domain "www.test4.com", address: "192.168.0.50", {
        user refdomain: domain3id
    }
}
