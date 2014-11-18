/*
 * Copyright 2013-2014 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.dns.maradns.ubuntu_12_04
dns {
	serial 1, generate: false

	// bind the dns server to address only
	bind address: "127.0.0.1"

    // adds IPv4 address alias
    alias "localhost", address: "127.0.0.1"
    alias "vbox", addresses: "10.0.2.2, 10.0.2.3"

    // sets the group to the root servers
    servers root: "icann"

    // sets allowed recursive hosts
    acls "localhost"

	// soa entry
	zone "example1.com", primary: "ns.example1.com", email: "hostmaster@example1.com"
}
