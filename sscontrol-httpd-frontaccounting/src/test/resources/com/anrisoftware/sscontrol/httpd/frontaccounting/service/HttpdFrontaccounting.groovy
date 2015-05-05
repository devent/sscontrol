/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-frontaccounting.
 *
 * sscontrol-httpd-frontaccounting is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-frontaccounting is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-frontaccounting. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.frontaccounting.service

httpd {
    domain "test1.com", address: "192.168.0.51", {
        setup "frontaccounting 2.3", id: "faccid", alias: "/account", prefix: "frontaccounting_2_3", ref: "faccidref", {
            debug "php", level: 1
            debug "sql", level: 1
            debug "go", level: 1
            debug "pdf", level: 1
            debug "sqltrail", level: 1
            debug "select", level: 1
            override mode: OverrideMode.update
            backup target: "$tmp/var/backups"
            database "faccountingdb", user: "faccountinguser", password: "faccountingpass", host: "localhost", port: 3306
            title "My Company Pvt Ltd"
            language locales: "de, pt"
        }
    }
}
