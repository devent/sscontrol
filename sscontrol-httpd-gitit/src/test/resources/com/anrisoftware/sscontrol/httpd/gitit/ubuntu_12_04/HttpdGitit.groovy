/*
 * Copyright 2012-2013 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.httpd.gitit.ubuntu_12_04

httpd {
    domain "test1.com", address: "192.168.0.51", {
        setup "gitit", id: "gititid", alias: "/", type: git, prefix: "gitit", {
            bind address: "127.0.0.1", port: 9999
            override mode: update
            caching enabled: yes
            idle gc: yes
        }
    }
    domain "www.test1.com", address: "192.168.0.51", {
        setup "gitit", ref: "gititid", refdomain: "testid"
    }
}
