/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-fudforum.
 *
 * sscontrol-httpd-fudforum is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-fudforum is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-fudforum. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.piwik.service

httpd {
    domain "test1.com", address: "192.168.0.51", {
        setup "fudforum", id: "fudforumid", alias: "/fudforum", prefix: "test1comfudforum", ref: "fudforumid", {
            debug "php", level: 1
            override mode: OverrideMode.update
            backup target: "$tmp/var/backups"
            database "fudforumdb", user: "fudforumuser", password: "fudforumpass", host: "localhost", port: 3306, prefix: "fudforum_", type: DatabaseType.mysql
            root "admin", password: "admin", email: "admin@server.com"
            site "http://127.0.0.1:8080/forum/"
            language "de"
            template "default"
        }
    }
}
