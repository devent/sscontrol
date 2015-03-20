/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-yourls.
 *
 * sscontrol-httpd-yourls is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-yourls is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-yourls. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.piwik.service

httpd {
    domain "test1.com", address: "192.168.0.51", {
        setup "yourls", id: "yourlsid", alias: "/yourls", prefix: "test1comyourls", ref: "yourlsid", {
            debug "php", level: 1
            override mode: OverrideMode.update
            backup target: "$tmp/var/backups"
            database "yourlsdb", user: "yourlsuser", password: "yourlspass", host: "localhost", port: 3306, prefix: "yourls_"
            access Access.open, stats: Access.open, api: Access.open
            user "admin", password: "mypass"
            user "foo", password: "foopass"
            user "bar", password: "barpass"
        }
    }
}
