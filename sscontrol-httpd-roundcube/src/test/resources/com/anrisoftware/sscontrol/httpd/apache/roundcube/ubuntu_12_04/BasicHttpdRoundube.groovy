/*
 * Copyright 2014-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-httpd-roundcube.
 *
 * sscontrol-httpd-roundcube is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-httpd-roundcube is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-httpd-roundcube. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.httpd.roundcube.service

def certFile = ServicesResources.class.getResource "cert_crt.txt"
def certKeyFile = ServicesResources.class.getResource "cert_key.txt"

httpd {
    domain "test1.com", address: "192.168.0.51", {
        setup "roundcube", id: "idroundcube", alias: "roundcube", {
            product name: "test1.com mail"
            database "roundcubedb", driver: "mysql", user: "userdb", password: "userpassdb", host: "localhost"
            mail "tls://%h", user: "usersmtp", password: "passwordsmtp"
            backup target: "$tmp/var/backups"
            server "default", port: 981
            server "Default Server", host: "mail.example.com"
            server "Webmail Server", host: "webmail.example.com"
            host "example.com", domain: "mail.example.com"
            host "otherdomain.com", domain: "othermail.example.com"
            plugins "archive, zipdownload, managesieve"
        }
    }
    ssl_domain "test1.com", address: "192.168.0.51", {
        certificate file: certFile, key: certKeyFile
        setup "roundcube", ref: "idroundcube"
    }
    domain "test2.com", address: "192.168.0.51", {
        setup "roundcube", id: "idroundcube", alias: "roundcubemin", {
            database "roundcubedb", driver: "mysql", user: "userdb", password: "userpassdb"
            server "Default Server", host: "mail.example.com"
            server "Webmail Server", host: "webmail.example.com"
            host "example.com", domain: "mail.example.com"
            host "otherdomain.com", domain: "othermail.example.com"
        }
    }
    domain "test3.com", address: "192.168.0.51", {
        setup "roundcube", id: "idroundcube", alias: "roundcubemin", {
            database "roundcubedb", driver: "mysql", user: "userdb", password: "userpassdb"
        }
    }
}
