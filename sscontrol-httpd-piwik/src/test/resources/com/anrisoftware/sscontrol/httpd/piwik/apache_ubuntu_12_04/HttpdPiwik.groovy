/*
 * Copyright 2014 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.httpd.piwik.apache_ubuntu_12_04

import com.anrisoftware.sscontrol.httpd.piwik.ubuntu_12_04.Ubuntu_12_04_Resources
import com.anrisoftware.sscontrol.httpd.webservice.OverrideMode

httpd {
    domain "test1.com", address: "192.168.0.51", {
        setup "piwik", id: "piwikid", alias: "/piwik", prefix: "test1piwik", {
            debug "php", level: 1
            debug "piwik", level: 4
            override mode: OverrideMode.update
            backup target: "$tmp/var/backups"
            database "piwik", user: "user", password: "userpass", host: "localhost", port: 3306, prefix: "piwik_", adapter: "PDO\\MYSQL", type: "InnoDB", schema: "Mysql"
        }
    }
    ssl_domain "test1.com", address: "192.168.0.51", {
        certificate file: Ubuntu_12_04_Resources.certCrt.resource, key: Ubuntu_12_04_Resources.certKey.resource
        setup "piwik", ref: "piwikid"
    }
    domain "test2.com", address: "192.168.0.51", {
        setup "piwik", {
            database "piwik", user: "user", password: "userpass"
        }
    }
}
