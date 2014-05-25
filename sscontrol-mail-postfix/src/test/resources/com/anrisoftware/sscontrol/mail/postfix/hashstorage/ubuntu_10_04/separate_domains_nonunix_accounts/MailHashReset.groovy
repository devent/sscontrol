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
package com.anrisoftware.sscontrol.mail.postfix.hashstorage.ubuntu_10_04.separate_domains_nonunix_accounts

mail {
    reset domains: yes

    bind address: all
    relay "smtp.relayhost.com"
    name "mail.example.com"
    origin "example.com"

    masquerade {
        domains "mail.example.com"
        users "root"
    }

    domain "example.com", {
        user "info"
        user "sales"
        user "disabled-user", { enabled false }
        alias "postmaster", destination: "postmaster"
        alias "disabled-alias", destination: "postmaster", { enabled false }
        catchall destination: "jim"
    }

    domain "disabled.domain", { enabled false }
}
