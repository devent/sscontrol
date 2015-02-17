/*
 * Copyright 2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-owncloud.
 *
 * sscontrol-httpd-owncloud is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-owncloud is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-owncloud. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.owncloud.service

import com.anrisoftware.sscontrol.httpd.webservice.OverrideMode

httpd {
    domain "test1.com", address: "192.168.0.51", {
        setup "owncloud_7", id: "owncloudid", alias: "/owncloud", prefix: "test1owncloud", ref: "owncloudid", {
            debug "php", level: 1
            debug "owncloud", level: 1
            override mode: OverrideMode.update
            backup target: "$tmp/var/backups"
            database "owncloud", user: "user", password: "userpass", host: "localhost", port: 3306, prefix: "owncloud_", adapter: "mysql"
        }
    }
}
