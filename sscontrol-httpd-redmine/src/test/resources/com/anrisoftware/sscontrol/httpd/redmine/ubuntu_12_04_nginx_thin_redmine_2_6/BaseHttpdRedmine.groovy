/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-redmine.
 *
 * sscontrol-httpd-redmine is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-redmine is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-redmine. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.redmine.nginx_ubuntu_12_04

import com.anrisoftware.sscontrol.httpd.ubuntu_12_04.Ubuntu_12_04_Resources

domainname = "example.net"

httpd {
    domain "test1.com", address: "192.168.0.51", {
        setup "redmine_2_6", backend: "thin", id: "redmineid", {
            debug "thin", level: 1, file: "/var/log/redmine_thin.log"
            debug "redmine", level: 1, file: "/var/log/redmine.log"
            override mode: OverrideMode.update
            backup target: "$tmp/var/backups"
            database "redmine2", user: "user", password: "userpass", host: "localhost"
            mail "smtp.test1.com", port: 25, method: DeliveryMethod.smtp, domain: "example.net", auth: AuthenticationMethod.login, user: "redmine@example.net", password: "redminepass"
            language name: "de"
            scm install: [
                ScmInstall.subversion,
                ScmInstall.mercurial
            ]
        }
    }
    ssl_domain "test1.com", address: "192.168.0.51", {
        certificate file: Ubuntu_12_04_Resources.certCrt.resource, key: Ubuntu_12_04_Resources.certKey.resource
        setup "redmine_2_6", backend: "thin", ref: "redmineid"
    }
}
