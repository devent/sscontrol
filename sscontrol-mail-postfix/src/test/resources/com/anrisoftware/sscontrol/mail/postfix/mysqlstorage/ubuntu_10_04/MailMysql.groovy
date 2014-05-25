/*
 * Copyright 2013-2014 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-mail-postfix.
 *
 * sscontrol-mail-postfix is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-mail-postfix is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-mail-postfix. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.mail.postfix.mysqlstorage.ubuntu_10_04

import static com.anrisoftware.sscontrol.mail.postfix.script.ubuntu_10_04.UbuntuResources.*

mail {
    bind address: all
    relay "smtp.relayhost.com"
    name "mail.example.com"
    origin "example.com"
    database "maildb", user: "root", password: "password"
    certificate cert: cert.resource, key: key.resource, ca: ca.resource

    masquerade {
        domains "mail.example.com"
        users "root"
    }

    domain "localhost.localdomain", { catchall destination: "@localhost" }
    domain "localhost", {
        alias "postmaster", destination: "root"
        alias "sysadmin", destination: "root"
        alias "webmaster", destination: "root"
        alias "abuse", destination: "root"
        alias "root", destination: "root"
        catchall destination: "root"
        user "root", password: "rootpasswd"
    }
}
