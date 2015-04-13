/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-piwik.
 *
 * sscontrol-httpd-piwik is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-piwik is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-piwik. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.piwik.service

import com.anrisoftware.sscontrol.httpd.webservice.OverrideMode

httpd {
    domain "test1.com", address: "192.168.0.51", {
        setup "piwik", id: "piwikid", alias: "/piwik", prefix: "test2piwik", ref: "piwikid", {
            debug "php", level: 1
            debug "piwik", level: 4, file: "tmp/logs/piwik.log", writer: "file"
            override mode: OverrideMode.update
            backup target: "$tmp/var/backups"
            database "piwik", user: "user", password: "userpass", host: "localhost", port: 3306, prefix: "piwik_", adapter: "PDO\\MYSQL", type: "InnoDB", schema: "Mysql"
        }
    }
}
