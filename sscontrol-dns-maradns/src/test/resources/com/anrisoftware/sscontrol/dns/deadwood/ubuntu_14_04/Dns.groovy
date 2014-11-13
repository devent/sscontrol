/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-dns-maradns.
 *
 * sscontrol-dns-maradns is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-dns-maradns is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-dns-maradns. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.dns.maradns.ubuntu_14_04
dns {

    // bind the dns server to address only
    bind address: "127.0.0.1"

    // sets the group to the root servers
    roots {
        // adds icann root servers
        servers group: "icann"

        // adds named root server
        server name: "example1.com", address: "127.0.0.2"
    }

    // sets addresses who is allowed to perform DNS recursion
    acls address: "127.0.0.1"

    // sets addresses who is allowed to perform DNS recursion
    acls addresses: "192.168.0.1, 192.168.0.2"

}
